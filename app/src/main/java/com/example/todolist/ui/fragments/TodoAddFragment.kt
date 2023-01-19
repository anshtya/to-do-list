package com.example.todolist.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolist.data.local.Todo
import com.example.todolist.databinding.FragmentTodoAddBinding
import com.example.todolist.ui.viewmodel.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoAddFragment : Fragment() {

    private lateinit var binding: FragmentTodoAddBinding
    private val viewModel: TodoViewModel by activityViewModels()

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
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.getTodo(id).collect {
                        bind(it)
                    }
                }
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
            val todoName = binding.txtEnterTodo.text.toString()
            viewModel.insertTodo(
                Todo(
                name = todoName
            )
            )
            binding.txtEnterTodo.text = null
            findNavController().navigateUp()
        }
    }

    private fun bind(todo: Todo) {
        binding.apply {
            txtEnterTodo.setText(todo.name)
            btnSaveTodo.setOnClickListener { viewModel.updateTodo(
                Todo(
                id = todo.id,
                name = txtEnterTodo.text.toString(),
                isDone = todo.isDone
            )
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