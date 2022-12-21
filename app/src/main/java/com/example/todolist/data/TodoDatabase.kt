package com.example.todolist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todolist.data.todo.Todo
import com.example.todolist.data.todo.TodoDao

@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao
    companion object{
        @Volatile
        private var INSTANCE: TodoDatabase? = null
        fun getDatabase(context: Context): TodoDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "item_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}