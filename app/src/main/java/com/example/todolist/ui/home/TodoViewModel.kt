package com.example.todolist.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.model.Response
import com.example.todolist.domain.model.Todo
import com.example.todolist.domain.TodosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todosUseCase: TodosUseCase
) : ViewModel() {

    private val _todos = MutableStateFlow<Response<List<Todo>>>(Response.Loading)
    val todos = _todos.asStateFlow()

    init {
        viewModelScope.launch {
            todosUseCase.getTodos().collect{ todos ->
                _todos.value = todos
            }
        }
    }

    fun insertTodo(todoName: String) = viewModelScope.launch {
        todosUseCase.insertTodo(todoName)
    }

    fun updateTodo(updatedTodo: Todo) = viewModelScope.launch {
        todosUseCase.updateTodo(updatedTodo)
    }

    fun deleteTodo(todoId: String) = viewModelScope.launch {
        todosUseCase.deleteTodo(todoId)
    }

}