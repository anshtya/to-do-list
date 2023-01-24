package com.example.todolist.util

sealed class Resource(val message: String? = null) {
    object Success : Resource()

    class Error(message: String?) : Resource(message)

    object Loading : Resource()
}