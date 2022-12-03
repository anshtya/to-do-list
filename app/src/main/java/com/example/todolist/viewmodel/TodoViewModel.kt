package com.example.todolist.viewmodel

import androidx.lifecycle.*
import com.example.todolist.data.todo.Todo
import com.example.todolist.data.todo.TodoDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TodoViewModel(private val TodoDao: TodoDao): ViewModel() {

    val allTodo: LiveData<List<Todo>> = TodoDao.getAll().asLiveData()

    fun insertTodo(todo: String){
        val newTodo = newTodoEntry(todo)
        insertTodo(newTodo)
    }

    fun updateTodo(todoId: Int, todoName: String, todoIsDone: Boolean){
        val updatedTodo = updateTodoEntry(todoId, todoName, todoIsDone)
        updateTodo(updatedTodo)
    }

    fun deleteTodo(todoId: Int, todoName: String, todoIsDone: Boolean){
        val deletedTodo = deleteTodoEntry(todoId, todoName, todoIsDone)
        deleteTodo(deletedTodo)
    }

    private fun newTodoEntry(todoName: String) : Todo{
        return Todo(name = todoName, isDone = false)
    }

    private fun updateTodoEntry(todoId: Int, todoName: String, todoIsDone: Boolean) : Todo{
        return Todo(id = todoId, name = todoName, isDone = todoIsDone)
    }

    private fun deleteTodoEntry(todoId: Int, todoName: String, todoIsDone: Boolean) : Todo{
        return Todo(id = todoId, name = todoName, isDone = todoIsDone)
    }

    private fun insertTodo(todo: Todo){
        viewModelScope.launch {
            TodoDao.insertTodo(todo)
        }
    }

    private fun updateTodo(todo: Todo){
        viewModelScope.launch {
            TodoDao.updateTodo(todo)
        }
    }

    private fun deleteTodo(todo: Todo){
        viewModelScope.launch {
            TodoDao.deleteTodo(todo)
        }
    }
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class TodoViewModelFactory(private val TodoDao: TodoDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(TodoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}