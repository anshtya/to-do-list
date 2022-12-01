package com.example.todolist.data.todo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true)val id: Int = 0 ,
    @ColumnInfo(name = "todo_name") val name: String,
    @ColumnInfo(name = "todo_done") val isDone: Boolean
)