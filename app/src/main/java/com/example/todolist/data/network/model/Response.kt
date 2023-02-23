package com.example.todolist.data.network.model

sealed class Response<out T> {

    data class Success<out T>(val data: T) : Response<T>()

    object Loading : Response<Nothing>()

    data class Error(val e: Exception?) : Response<Nothing>()
}