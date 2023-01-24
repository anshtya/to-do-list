package com.example.todolist.util

import com.example.todolist.data.network.User

sealed class Resource(
    val message: String? = null,
    val data: User? = null
) {
    class Success(data: User? = null) : Resource(message = null, data)

    class Error(message: String?) : Resource(message)

    object Loading : Resource()
}