package com.example.todolist.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(todo: Todo)
    @Delete
    fun delete(todo: Todo)
    @Query("SELECT * FROM todo")
    fun getAll(): List<Todo>
}