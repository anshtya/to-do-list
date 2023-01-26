package com.example.todolist.data.local

data class Todo(
    var id: Int,
    var name: String,
    var isDone: Boolean = false
)