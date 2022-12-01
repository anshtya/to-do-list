package com.example.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todolist.databinding.FragmentTodoAddBinding
import com.example.todolist.viewmodel.TodoViewModel
import com.example.todolist.viewmodel.TodoViewModelFactory

class TodoAddFragment : Fragment() {

    private lateinit var binding: FragmentTodoAddBinding
    private val viewmodel: TodoViewModel by activityViewModels{
        TodoViewModelFactory(
            (activity?.application as TodoApplication).database
                .todoDao()
        )
    }

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
            addNewTodo()
        }
    }

    private fun addNewTodo(){
        if (isEmpty()) {
            binding.txtEnterTodo.editText?.text = null
        } else {
            val todo = binding.txtEnterTodo.editText?.text.toString()
            viewmodel.insertTodo(todo)
            binding.txtEnterTodo.editText?.text = null

            val action = TodoAddFragmentDirections.actionTodoAddFragmentToTodoFragment()
            findNavController().navigate(action)
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