package com.example.todolist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.TodoViewBinding
import com.example.todolist.todoList

class TodoAdapter(private val listener: (Int) -> Unit) : RecyclerView.Adapter<ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = TodoViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemViewHolder = ItemViewHolder(binding)
        itemViewHolder.btnDelete.setOnClickListener {
            listener(itemViewHolder.adapterPosition)
            notifyItemRemoved(itemViewHolder.adapterPosition)
        }
        return itemViewHolder
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.tvItem.text = todoList[position]
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

}
class ItemViewHolder(private val binding: TodoViewBinding) : RecyclerView.ViewHolder(binding.root) {
    val tvItem = binding.tvItem
    val btnDelete = binding.btnDelete
}
interface TodoDelete{
    fun onTodoDelete(position: Int)
}