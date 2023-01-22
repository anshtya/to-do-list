package com.example.todolist.ui.home

import androidx.lifecycle.*
import com.example.todolist.data.local.Todo
import com.example.todolist.data.repositories.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repository: TodoRepository
    ) : ViewModel() {

    val allTodos = repository.getAllTodos().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun getTodo(id: Int) = repository.getTodo(id)

    fun insertTodo(newTodo: Todo){
        viewModelScope.launch {
            repository.insert(newTodo)
        }
    }

    fun updateTodo(updatedTodo: Todo){
        viewModelScope.launch {
            repository.update(updatedTodo)
        }
    }

    fun deleteTodo(todo: Todo){
        viewModelScope.launch {
            repository.delete(todo)
        }
    }
}