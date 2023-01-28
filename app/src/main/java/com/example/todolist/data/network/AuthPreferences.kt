package com.example.todolist.data.network

data class AuthPreferences(
    val password: String,
    val authGoogle: Boolean,
    val authEmail: Boolean
)