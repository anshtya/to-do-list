package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import com.example.todolist.adapter.TodoDelete
import com.example.todolist.todoList

class TodoViewmodel: ViewModel(), TodoDelete {

    fun insertTodo(task: String){
        todoList.add(task)
    }

    override fun onTodoDelete(position: Int) {
        todoList.removeAt(position)
    }
}