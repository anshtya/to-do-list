package com.example.todolist

import android.app.Application
import com.example.todolist.data.TodoDatabase

class TodoApplication: Application() {
    val database: TodoDatabase by lazy { TodoDatabase.getDatabase(this) }
}