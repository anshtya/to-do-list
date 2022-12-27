package com.example.todolist.viewmodel

import androidx.lifecycle.*
import com.example.todolist.data.todo.Todo
import com.example.todolist.repository.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository): ViewModel() {

    fun todos() = repository.getAllTodos()

    fun getTodo(id: Int): LiveData<Todo>{
        return repository.getTodo(id)
    }

    fun insertTodo(todoName: String){
        viewModelScope.launch {
            val newTodo = Todo(name = todoName)
            repository.insert(newTodo)
        }
    }

    fun updateTodo(todoId: Int, todoName: String, todoIsDone: Boolean){
        viewModelScope.launch {
            val updatedTodo = Todo(id = todoId, name = todoName, isDone = todoIsDone)
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