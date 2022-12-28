package com.example.todolist.viewmodel

import androidx.lifecycle.*
import com.example.todolist.data.entity.Todo
import com.example.todolist.repository.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository): ViewModel() {

    fun todos() = repository.getAllTodos().asLiveData()

    fun getTodo(id: Int): LiveData<Todo>{
        return repository.getTodo(id).asLiveData()
    }

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

class TodoViewModelFactory(private val repository: TodoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}