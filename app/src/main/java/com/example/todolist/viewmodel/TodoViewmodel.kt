package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import com.example.todolist.TodoDelete
import com.example.todolist.todoList

class TodoViewmodel: ViewModel(), TodoDelete {

    /*
    * Function to insert todo
    */
    fun insertTodo(task: String){
        todoList.add(task)
    }

    /*
    * Function to delete todo
    */
    override fun onTodoDelete(position: Int) {
        todoList.removeAt(position)
    }
}