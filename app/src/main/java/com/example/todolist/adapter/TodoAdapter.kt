package com.example.todolist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.data.entity.Todo
import com.example.todolist.databinding.TodoViewBinding
import com.example.todolist.fragments.TodoFragment

class TodoAdapter(private val listener: TodoFragment):
    ListAdapter<Todo, TodoAdapter.TodoViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Todo>() {
            override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class TodoViewHolder(private val binding: TodoViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(todo: Todo){
            binding.apply {
                tvTodo.text = todo.name
                chkIsDone.isChecked = todo.isDone
                chkIsDone.setOnClickListener {
                    if (chkIsDone.isChecked) {
                        listener.onTodoUpdate(
                            Todo(
                            id = todo.id,
                            name = todo.name,
                            isDone = true
                        )
                        )
                    } else {
                        listener.onTodoUpdate(
                            Todo(
                            id = todo.id,
                            name = todo.name,
                            isDone = false
                        )
                        )
                    }
                }
                root.rootView.setOnLongClickListener {
                    listener.callTodoDialog(todo)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TodoViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currTodo = getItem(position)
        holder.bind(currTodo)
    }
}
interface TodoEvents{
    fun onTodoUpdate(todo: Todo)
    fun callTodoDialog(todo: Todo)
}