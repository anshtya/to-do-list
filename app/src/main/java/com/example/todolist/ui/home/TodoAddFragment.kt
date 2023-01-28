package com.example.todolist.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolist.data.network.Todo
import com.example.todolist.databinding.FragmentTodoAddBinding
import com.example.todolist.util.Resource
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
        val todo = navigationArgs.todo
        if(todo.id != ""){
            bind(todo)
        } else {
            binding.btnSaveTodo.setOnClickListener {
                addNewTodo()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.todoStatus.collect{
                when(it){
                    is Resource.Loading -> {
                        binding.apply {
                            btnSaveTodo.visibility = View.INVISIBLE
                            todoProgressBar.visibility = View.VISIBLE
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun addNewTodo(){
        binding.apply {
            if (txtEnterTodo.text.toString().isEmpty()) {
                txtEnterTodo.text = null
            } else {
                val todoName = txtEnterTodo.text.toString()
                viewModel.insertTodo(todoName)
                findNavController().navigateUp()
            }
        }
    }

    private fun bind(todo: Todo) {
        binding.apply {
            txtEnterTodo.setText(todo.name)
            btnSaveTodo.setOnClickListener {
                viewModel.updateTodo(
                    Todo(
                        id = todo.id,
                        name = txtEnterTodo.text.toString(),
                        done = todo.done
                    )
                )
                findNavController().navigateUp()
            }
        }
    }
}