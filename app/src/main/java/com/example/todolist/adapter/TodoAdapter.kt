package com.example.todolist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.data.todo.Todo
import com.example.todolist.databinding.TodoViewBinding
import com.example.todolist.fragments.TodoFragment

class TodoAdapter(private val listener: TodoFragment):
    ListAdapter<Todo, TodoAdapter.TodoViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Todo>() {
            override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }

    init{
        setHasStableIds(true)
    }

    class TodoViewHolder(binding: TodoViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvTodo = binding.tvTodo
        val chkIsDone = binding.chkIsDone
        val btnDelete = binding.btnDelete
        fun bind(todo: Todo){
            tvTodo.text = todo.name
            chkIsDone.isChecked = todo.isDone
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TodoViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currTodo = getItem(position)
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
                listener.onTodoDelete(currTodo)
            }
            tvTodo.setOnClickListener {
                listener.onTodoEdit(currTodo)
            }
        }

    }

    override fun getItemId(position: Int): Long = position.toLong()

}
interface TodoEvents{
    fun onTodoUpdate(todoId: Int, todoName: String, todoIsDone: Boolean)
    fun onTodoDelete(todo: Todo)
    fun onTodoEdit(todo: Todo)
}