package com.example.todolist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.TodoApplication
import com.example.todolist.adapter.TodoAdapter
import com.example.todolist.adapter.TodoEvents
import com.example.todolist.data.todo.Todo
import com.example.todolist.databinding.FragmentTodoBinding
import com.example.todolist.repository.TodoRepository
import com.example.todolist.viewmodel.TodoViewModel
import com.example.todolist.viewmodel.TodoViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class TodoFragment : Fragment(), TodoEvents{

    private lateinit var binding: FragmentTodoBinding
    private lateinit var listRecyclerView: RecyclerView
    private lateinit var todoAdapter: TodoAdapter
    private val viewModel: TodoViewModel by activityViewModels{
        TodoViewModelFactory(
            TodoRepository(
                (activity?.application as TodoApplication).database.todoDao()
            )
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
        viewModel.todos().observe(viewLifecycleOwner) {
            todoAdapter.submitList(it)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val currTodo = todoAdapter.currentList[position]
                viewModel.deleteTodo(currTodo)
                Snackbar.make(view, "Article deleted successfully", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        val undoTodo = Todo(id = currTodo.id, name = currTodo.name)
                        viewModel.insertTodo(undoTodo)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(listRecyclerView)
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
        todoAdapter = TodoAdapter(this)
        listRecyclerView = binding.listRecyclerView
        listRecyclerView.apply{
            adapter = todoAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun onTodoEdit(todoId: Int) {
        val action = TodoFragmentDirections.actionTodoFragmentToTodoAddFragment(
            getString(R.string.edit),
            todoId
        )
        findNavController().navigate(action)
    }

    override fun onTodoUpdate(todo: Todo) {
        viewModel.updateTodo(todo)
    }
    override fun callTodoDialog(todo: Todo) {
        showTodoDialog(todo)
    }
}