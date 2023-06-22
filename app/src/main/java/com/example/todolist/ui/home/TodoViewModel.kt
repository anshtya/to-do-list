package com.example.todolist.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.network.model.Response
import com.example.todolist.data.network.model.Todo
import com.example.todolist.data.repositories.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _todos = MutableStateFlow<Response<List<Todo>>>(Response.Loading)
    val todos = _todos.asStateFlow()

    init {
        viewModelScope.launch {
            todoRepository.getTodos().collect{ todos ->
                _todos.value = todos
            }
        }
    }

    fun insertTodo(todoName: String) = viewModelScope.launch {
        todoRepository.insertTodo(todoName)
    }

    fun updateTodo(updatedTodo: Todo) = viewModelScope.launch {
        todoRepository.updateTodo(updatedTodo)
    }

    fun deleteTodo(todoId: String) = viewModelScope.launch {
        todoRepository.deleteTodo(todoId)
    }

}