package com.example.todolist.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.network.Todo
import com.example.todolist.domain.TodosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todosUseCase: TodosUseCase
) : ViewModel() {

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>>
        get() = _todos

    init {
        getAllTodo()
    }

    private fun getAllTodo() = viewModelScope.launch {
        todosUseCase.todos.collect{ todos ->
            _todos.value = todos
        }
    }

    fun insertTodo(todoName: String) = viewModelScope.launch {
        todosUseCase.insertTodo(todoName)
        getAllTodo()
    }

    fun updateTodo(updatedTodo: Todo) = viewModelScope.launch {
        todosUseCase.updateTodo(updatedTodo)
        getAllTodo()
    }

    fun deleteTodo(todoId: String) = viewModelScope.launch {
        todosUseCase.deleteTodo(todoId)
        getAllTodo()
    }

}