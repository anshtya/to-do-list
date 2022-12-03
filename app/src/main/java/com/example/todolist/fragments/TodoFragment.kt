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
import com.example.todolist.TodoApplication
import com.example.todolist.adapter.TodoAdapter
import com.example.todolist.adapter.TodoOps
import com.example.todolist.databinding.FragmentTodoBinding
import com.example.todolist.viewmodel.TodoViewModel
import com.example.todolist.viewmodel.TodoViewModelFactory

class TodoFragment : Fragment(), TodoOps {

    private lateinit var binding: FragmentTodoBinding
    private lateinit var listRecyclerView: RecyclerView
    private val adapter = TodoAdapter(this)
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
        viewModel.allTodo.observe(this.viewLifecycleOwner) { todos ->
            todos?.let { adapter.submitList(it) }
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val currTodo = adapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteTodo(currTodo.id, currTodo.name, currTodo.isDone)
            }
        }).attachToRecyclerView(listRecyclerView)


        binding.btnAddTodo.setOnClickListener {
            val action = TodoFragmentDirections.actionTodoFragmentToTodoAddFragment()
            findNavController().navigate(action)
        }
    }

    private fun setRecyclerView(){
        listRecyclerView = binding.listRecyclerView
        listRecyclerView.layoutManager = LinearLayoutManager(context)
        listRecyclerView.adapter = adapter
        listRecyclerView.setHasFixedSize(true)
    }

    override fun onTodoUpdate(todoId: Int, todoName: String, todoIsDone: Boolean) {
        viewModel.updateTodo(todoId, todoName, todoIsDone)
    }
}