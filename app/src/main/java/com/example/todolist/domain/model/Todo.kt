package com.example.todolist.domain.model

import java.io.Serializable

data class Todo(
    val id: String = "",
    val name: String = "",
    val createdBy: User = User(),
    val done: Boolean = false
) : Serializable