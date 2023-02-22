package com.example.todolist.data.network.model

data class AuthPreferences(
    val password: String,
    val authGoogle: Boolean,
    val authEmail: Boolean
)