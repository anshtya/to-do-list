package com.example.todolist.domain

import com.example.todolist.data.repositories.TodoRepository
import com.example.todolist.domain.model.Todo
import javax.inject.Inject

class TodosUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    fun getTodos() = todoRepository.getTodos()

    suspend fun deleteTodo(todoId: String) = todoRepository.deleteTodo(todoId)

    suspend fun insertTodo(todoName: String) = todoRepository.insertTodo(todoName)

    suspend fun updateTodo(updatedTodo: Todo) = todoRepository.updateTodo(updatedTodo)
}