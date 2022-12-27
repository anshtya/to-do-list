package com.example.todolist.viewmodel

import androidx.lifecycle.*
import com.example.todolist.data.todo.Todo
import com.example.todolist.repository.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository): ViewModel() {

    fun todos() = repository.getAllTodos().asLiveData()

    fun insertTodo(todo: String){
        val newTodo = newTodoEntry(todo)
        insertTodo(newTodo)
    }

    fun updateTodo(todoId: Int, todoName: String, todoIsDone: Boolean){
        val updatedTodo = updateTodoEntry(todoId, todoName, todoIsDone)
        updateTodo(updatedTodo)
    }

    fun deleteTodo(todo: Todo){
        daoDeleteTodo(todo)
    }

    fun getTodo(id: Int): LiveData<Todo>{
        return repository.getTodo(id).asLiveData()
    }

    private fun newTodoEntry(todoName: String) : Todo{
        return Todo(name = todoName)
    }

    private fun updateTodoEntry(todoId: Int, todoName: String, todoIsDone: Boolean) : Todo{
        return Todo(id = todoId, name = todoName, isDone = todoIsDone)
    }

    private fun insertTodo(todo: Todo){
        viewModelScope.launch {
            repository.insert(todo)
        }
    }

    private fun updateTodo(todo: Todo){
        viewModelScope.launch {
            repository.update(todo)
        }
    }

    private fun daoDeleteTodo(todo: Todo){
        viewModelScope.launch {
            repository.delete(todo)
        }
    }
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class TodoViewModelFactory(private val repository: TodoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}