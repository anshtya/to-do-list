package com.example.todolist.data.network.model

sealed class AuthResult {
    object Success : AuthResult()

    class Error(val e: Exception) : AuthResult()

    object Loading : AuthResult()
}