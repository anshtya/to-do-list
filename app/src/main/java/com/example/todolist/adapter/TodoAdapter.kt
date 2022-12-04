package com.example.todolist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.data.todo.Todo
import com.example.todolist.databinding.TodoViewBinding
import com.example.todolist.fragments.TodoFragment

class TodoAdapter(var todoList: List<Todo> ,private val listener: TodoFragment):
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    init{
        setHasStableIds(true)
    }

    class TodoViewHolder(private val binding: TodoViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val chkIsDone = binding.chkIsDone
        val btnDelete = binding.btnDelete
        fun bind(todo: Todo){
            binding.tvTodo.text = todo.name
            chkIsDone.isChecked = todo.isDone
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TodoViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currTodo = todoList[position]
        holder.apply{
            bind(currTodo)
            chkIsDone.setOnClickListener {
                if (chkIsDone.isChecked) {
                    listener.onTodoUpdate(currTodo.id, currTodo.name, true )
                } else {
                    listener.onTodoUpdate(currTodo.id, currTodo.name, false)
                }
            }
            btnDelete.setOnClickListener {
                listener.onTodoDelete(currTodo.id, currTodo.name, currTodo.isDone)
            }
        }

    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemCount(): Int {
        return todoList.size
    }
}
interface TodoEvents{
    fun onTodoUpdate(todoId: Int, todoName: String, todoIsDone: Boolean)
    fun onTodoDelete(todoId: Int, todoName: String, todoIsDone: Boolean)
}