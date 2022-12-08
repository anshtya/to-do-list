package com.example.todolist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.TodoApplication
import com.example.todolist.adapter.TodoAdapter
import com.example.todolist.adapter.TodoEvents
import com.example.todolist.data.todo.Todo
import com.example.todolist.databinding.FragmentTodoBinding
import com.example.todolist.viewmodel.TodoViewModel
import com.example.todolist.viewmodel.TodoViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TodoFragment : Fragment(), TodoEvents{

    private lateinit var binding: FragmentTodoBinding
    private lateinit var listRecyclerView: RecyclerView
    private lateinit var adapter: TodoAdapter
    private val viewModel: TodoViewModel by activityViewModels{
        TodoViewModelFactory(
            (activity?.application as TodoApplication).database
                .todoDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        viewModel.allTodo.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.btnAddTodo.setOnClickListener {
            val action = TodoFragmentDirections.actionTodoFragmentToTodoAddFragment(
                getString(R.string.add)
            )
            findNavController().navigate(action)
        }
    }

    private fun showTodoDialog(todo: Todo) {
        val options = arrayOf("Edit","Delete")
        MaterialAlertDialogBuilder(requireContext())
            .setCancelable(true)
            .setItems(options){ _,which ->
                when(options[which]){
                    "Edit" -> onTodoEdit(todo.id)
                    "Delete" -> viewModel.deleteTodo(todo)
                }
            }
            .show()
    }

    private fun setRecyclerView(){
        adapter = TodoAdapter(this)
        listRecyclerView = binding.listRecyclerView
        listRecyclerView.layoutManager = LinearLayoutManager(context)
        listRecyclerView.adapter = adapter
        listRecyclerView.setHasFixedSize(true)
    }

    private fun onTodoEdit(todoId: Int) {
        val action = TodoFragmentDirections.actionTodoFragmentToTodoAddFragment(
            getString(R.string.edit),
            todoId
        )
        findNavController().navigate(action)
    }

    override fun onTodoUpdate(todoId: Int, todoName: String, todoIsDone: Boolean) {
        viewModel.updateTodo(todoId, todoName, todoIsDone)
    }
    override fun callTodoDialog(position: Int) {
        val todo = adapter.currentList[position]
        showTodoDialog(todo)
    }
}