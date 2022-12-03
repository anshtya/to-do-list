package com.example.todolist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.TodoFragment
import com.example.todolist.data.todo.Todo
import com.example.todolist.databinding.TodoViewBinding

class TodoAdapter(private val listener: TodoFragment): ListAdapter<Todo, TodoAdapter.TodoViewHolder>(DiffCallback) {

    init{
        setHasStableIds(true)
    }

    class TodoViewHolder(private val binding: TodoViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val chkIsDone = binding.chkIsDone
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
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()

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

}
interface TodoOps{
    fun onTodoUpdate(todoId: Int, todoName: String, todoIsDone: Boolean)
}