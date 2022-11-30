package com.example.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todolist.databinding.FragmentTodoAddBinding
import com.example.todolist.viewmodel.TodoViewmodel

class TodoAddFragment : Fragment() {

    private lateinit var binding: FragmentTodoAddBinding
    private val viewmodel: TodoViewmodel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSaveTodo.setOnClickListener {
            if (isEmpty()) {
                binding.txtEnterTodo.editText?.text = null
            } else {
                val task = binding.txtEnterTodo.editText?.text.toString()
                viewmodel.insertTodo(task)
                binding.txtEnterTodo.editText?.text = null
                findNavController().navigate(R.id.action_todoAddFragment_to_todoFragment)
            }
        }
    }

    /*
    * Checks if EditText is empty or not
     */
    private fun isEmpty(): Boolean{
        if(binding.txtEnterTodo.editText?.text.toString().trim().isEmpty()) {
            return true
        }
        return false
    }
}