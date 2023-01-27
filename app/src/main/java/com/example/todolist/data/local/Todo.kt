package com.example.todolist.data.local

import com.example.todolist.data.network.User
import java.io.Serializable

data class Todo(
    val id: String = "",
    val name: String = "",
    val createdBy: User = User(),
    val done: Boolean = false
) : Serializable