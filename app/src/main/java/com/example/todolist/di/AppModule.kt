package com.example.todolist.di

import android.content.Context
import androidx.room.Room
import com.example.todolist.data.local.TodoDatabase
import com.example.todolist.data.local.TodoDao
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(
            app,
            TodoDatabase::class.java,
            "article_db.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideDao(db: TodoDatabase): TodoDao = db.todoDao()

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
}
