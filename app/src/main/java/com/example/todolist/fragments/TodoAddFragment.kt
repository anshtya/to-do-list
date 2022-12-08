package com.example.todolist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolist.TodoApplication
import com.example.todolist.data.todo.Todo
import com.example.todolist.databinding.FragmentTodoAddBinding
import com.example.todolist.viewmodel.TodoViewModel
import com.example.todolist.viewmodel.TodoViewModelFactory

class TodoAddFragment : Fragment() {

    private lateinit var binding: FragmentTodoAddBinding
    private val viewModel: TodoViewModel by activityViewModels{
        TodoViewModelFactory(
            (activity?.application as TodoApplication).database
                .todoDao()
        )
    }

    private val navigationArgs: TodoAddFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.todoId
        if(id > 0){
            viewModel.getTodo(id).observe(viewLifecycleOwner){
                bind(it)
            }
        } else {
            binding.btnSaveTodo.setOnClickListener {
                addNewTodo()
            }
        }
    }

    private fun addNewTodo(){
        if (isEmpty()) {
            binding.txtEnterTodo.text = null
        } else {
            val todo = binding.txtEnterTodo.text.toString()
            viewModel.insertTodo(todo)
            binding.txtEnterTodo.text = null
            findNavController().navigateUp()
        }
    }

    private fun bind(todo: Todo) {
        binding.apply {
            txtEnterTodo.setText(todo.name)
            btnSaveTodo.setOnClickListener { viewModel.updateTodo(
                todo.id,
                todoName = txtEnterTodo.text.toString(),
                todo.isDone
            )
                val action = TodoAddFragmentDirections.actionTodoAddFragmentToTodoFragment()
                findNavController().navigate(action)
            }
        }
    }

    /*
    * Checks if EditText is empty or not
     */
    private fun isEmpty(): Boolean{
        if(binding.txtEnterTodo.text.toString().trim().isEmpty()) {
            return true
        }
        return false
    }
}