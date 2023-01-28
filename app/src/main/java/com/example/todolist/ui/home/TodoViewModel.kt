package com.example.todolist.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.network.Todo
import com.example.todolist.data.repositories.TodoRepository
import com.example.todolist.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
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

    private val _todoStatus = MutableSharedFlow<Resource>()
    val todoStatus: SharedFlow<Resource>
        get() = _todoStatus

    init {
        getAllTodo()
    }

    private fun getAllTodo() = viewModelScope.launch {
        _todos.value = todoRepository.getAllTodo()
    }

    fun insertTodo(todoName: String) = viewModelScope.launch {
        _todoStatus.emit(Resource.Loading())
        todoRepository.insertTodo(todoName)
        getAllTodo()
    }

    fun updateTodo(updatedTodo: Todo) = viewModelScope.launch {
        _todoStatus.emit(Resource.Loading())
        todoRepository.updateTodo(updatedTodo)
        getAllTodo()
    }

    fun deleteTodo(todoId: String) = viewModelScope.launch {
        todoRepository.deleteTodo(todoId)
        getAllTodo()
    }

}