package com.example.todolist.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.local.Todo
import com.example.todolist.data.repositories.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>>
        get() = _todos

    init {
        getAllTodo()
    }

    private fun getAllTodo() = viewModelScope.launch {
        _todos.value = todoRepository.getAllTodo()
    }

    fun insertTodo(todoName: String) = viewModelScope.launch {
        todoRepository.insertTodo(todoName)
        getAllTodo()
    }

    fun updateTodo(updatedTodo: Todo) = viewModelScope.launch {
        todoRepository.updateTodo(updatedTodo)
        getAllTodo()
    }

    fun deleteTodo(todoId: String) = viewModelScope.launch {
        todoRepository.deleteTodo(todoId)
        getAllTodo()
    }

}