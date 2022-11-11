package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ItemDelete {

    private lateinit var binding: ActivityMainBinding
    private lateinit var listRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = TodoAdapter(this)
        listRecyclerView = binding.listRecyclerView
        listRecyclerView.layoutManager = LinearLayoutManager(this)
        listRecyclerView.adapter = adapter
        listRecyclerView.setHasFixedSize(true)

        val btnAddTodo = binding.btnAddTodo
        btnAddTodo.setOnClickListener {
            if (isEmpty()) {
                binding.txtItem.setText("")
            } else {
                val task = binding.txtItem.text.toString()
                todoList.add(task)
                binding.txtItem.setText("")
                adapter.notifyItemInserted(adapter.itemCount)
            }
        }
    }

    private fun isEmpty(): Boolean{
        if(binding.txtItem.text.toString().trim().isEmpty()) {
            return true
        }
        return false
    }

    override fun onItemDelete(position: Int) {
        todoList.removeAt(position)
    }
}