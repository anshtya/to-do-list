package com.example.todolist.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true)var id: Int = 0,
    @ColumnInfo(name = "todo_name") var name: String,
    @ColumnInfo(name = "todo_done") var isDone: Boolean = false
)