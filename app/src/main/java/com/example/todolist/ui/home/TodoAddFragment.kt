package com.example.todolist.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolist.data.network.model.Todo
import com.example.todolist.databinding.FragmentTodoAddBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoAddFragment : Fragment() {

    private var _binding: FragmentTodoAddBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TodoViewModel by activityViewModels()

    private val navigationArgs: TodoAddFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar(title = navigationArgs.title)
        val todo = navigationArgs.todo
        if(todo.id != ""){
            bind(todo)
        } else {
            binding.btnSaveTodo.setOnClickListener {
                addNewTodo()
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

    private fun setToolbar(title: String) {
        binding.todoAddFragmentToolbar.title = title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}