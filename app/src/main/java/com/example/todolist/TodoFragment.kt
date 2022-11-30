package com.example.todolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.FragmentTodoBinding
import com.example.todolist.viewmodel.TodoViewmodel

class TodoFragment : Fragment() {

    private lateinit var binding: FragmentTodoBinding
    private lateinit var listRecyclerView: RecyclerView
    private val viewmodel: TodoViewmodel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TodoAdapter{
            viewmodel.onTodoDelete(it)
        }
        listRecyclerView = binding.listRecyclerView
        listRecyclerView.layoutManager = LinearLayoutManager(context)
        listRecyclerView.adapter = adapter
        listRecyclerView.setHasFixedSize(true)

        binding.btnAddTodo.setOnClickListener {
            if (isEmpty()) {
                binding.txtItem.text = null
            } else {
                val task = binding.txtItem.text.toString()
                viewmodel.insertTodo(task)
                binding.txtItem.text = null
                adapter.notifyItemInserted(adapter.itemCount)
            }
        }
    }

    /*
    * Checks if EditText is empty or not
     */
    private fun isEmpty(): Boolean{
        if(binding.txtItem.text.toString().trim().isEmpty()) {
            return true
        }
        return false
    }
}