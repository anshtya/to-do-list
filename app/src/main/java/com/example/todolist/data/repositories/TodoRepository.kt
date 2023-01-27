package com.example.todolist.data.repositories

import com.example.todolist.data.local.Todo
import com.example.todolist.data.network.User
import com.example.todolist.util.Constants.Companion.TODOS
import com.example.todolist.util.Constants.Companion.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoRepository @Inject constructor(
    db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val todoCollection = db.collection(TODOS)
    private val userCollection = db.collection(USERS)

    suspend fun getAllTodo(): List<Todo> = withContext(Dispatchers.IO) {
        val userId = auth.currentUser!!.uid
        return@withContext todoCollection.whereEqualTo("createdBy.userId", userId).get().await()
            .toObjects(Todo::class.java)
    }

    suspend fun insertTodo(todoName: String) {
        val todoId = todoCollection.document().id
        val userId = auth.currentUser!!.uid
        withContext(Dispatchers.IO) {
            val currentUser = userCollection.document(userId).get().await()
                .toObject(User::class.java)!!

            val todo = Todo(todoId, todoName, currentUser)
            todoCollection.document(todoId).set(todo).await()
        }
    }

    suspend fun updateTodo(todo: Todo): Unit = withContext(Dispatchers.IO){
        todoCollection.document(todo.id).update(mapOf(
            "name" to todo.name,
            "done" to todo.done
        )).await()
    }

    suspend fun deleteTodo(todoId: String) = withContext(Dispatchers.IO){
        todoCollection.document(todoId).delete()
    }
}
