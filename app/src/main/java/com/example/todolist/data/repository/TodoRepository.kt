package com.example.todolist.data.repository

import com.example.todolist.data.entity.Todo
import com.example.todolist.data.dao.TodoDao

class TodoRepository(
    private val dao: TodoDao
) {
    fun getAllTodos() = dao.getAll()

    fun getTodo(id: Int) =  dao.getTodo(id)

    suspend fun insert(todo: Todo) = dao.insertTodo(todo)

    suspend fun delete(todo: Todo) = dao.deleteTodo(todo)

    suspend fun update(todo: Todo) = dao.updateTodo(todo)
}