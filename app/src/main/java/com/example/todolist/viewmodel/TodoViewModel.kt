package com.example.todolist.viewmodel

import androidx.lifecycle.*
import com.example.todolist.data.todo.Todo
import com.example.todolist.data.todo.TodoDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TodoViewModel(private val TodoDao: TodoDao): ViewModel() {

    val allTodo: Flow<List<Todo>> = TodoDao.getAll()

    fun insertTodo(todo: String){
        val newTodo = newTodoEntry(todo)
        insertTodo(newTodo)
    }

//    override fun onTodoDelete(position: Int) {
//        todoList.removeAt(position)
//    }

    private fun newTodoEntry(todoName: String) : Todo{
        return Todo(name = todoName, isDone = false)
    }

    private fun insertTodo(todo: Todo){
        viewModelScope.launch {
            TodoDao.insertTodo(todo)
        }
    }

    fun deleteTodo(todo: Todo){
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