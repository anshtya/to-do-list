package com.example.todolist.data

import com.example.todolist.data.local.Todo
import com.example.todolist.data.local.TodoDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoRepository @Inject constructor(
    private val dao: TodoDao
) {
    fun getAllTodos() = dao.getAll()

    fun getTodo(id: Int): Flow<Todo> {
        return dao.getTodo(id)
    }

    suspend fun insert(todo: Todo) = dao.insertTodo(todo)

    suspend fun delete(todo: Todo) = dao.deleteTodo(todo)

    suspend fun update(todo: Todo) = dao.updateTodo(todo)
}