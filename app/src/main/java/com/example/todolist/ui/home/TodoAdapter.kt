package com.example.todolist.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.data.local.Todo
import com.example.todolist.databinding.TodoViewBinding

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

        private var currTodo: Todo? = null

        init{
            binding.apply{
                chkIsDone.setOnClickListener {
                    currTodo?.let { todo ->
                        if (chkIsDone.isChecked) {
                            listener.onTodoUpdate(
                                Todo(id = todo.id, name = todo.name, done = true)
                            )
                        } else {
                            listener.onTodoUpdate(
                                Todo(id = todo.id, name = todo.name, done = false)
                            )
                        }
                    }

                }
                root.rootView.setOnLongClickListener {
                    currTodo?.let { todo ->
                        listener.callTodoDialog(todo)
                    }
                    true
                }
            }
        }

        fun bind(todo: Todo){
            currTodo = todo
            binding.apply {
                tvTodo.text = todo.name
                chkIsDone.isChecked = todo.done
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