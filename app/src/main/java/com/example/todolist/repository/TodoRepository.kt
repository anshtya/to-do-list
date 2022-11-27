package com.example.todolist.repository

import com.example.todolist.data.todo.Todo
import com.example.todolist.data.todo.TodoDao
import kotlinx.coroutines.flow.Flow

class TodoRepository(private val dao: TodoDao) {
    suspend fun insertTodo(todo: Todo){
        dao.insertTodo(todo)
    }

    suspend fun deleteTodo(todo: Todo){
        dao.deleteTodo(todo)
    }

    fun getTodo(id: Int): Todo?{
        return dao.getTodo(id)
    }

    fun getAll(): Flow<List<Todo>>{
        return dao.getAll()
    }
}