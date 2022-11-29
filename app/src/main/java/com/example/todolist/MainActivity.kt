package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.viewmodel.TodoViewmodel

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var listRecyclerView: RecyclerView
    private val viewmodel: TodoViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = TodoAdapter(viewmodel)
        listRecyclerView = binding.listRecyclerView
        listRecyclerView.layoutManager = LinearLayoutManager(this)
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