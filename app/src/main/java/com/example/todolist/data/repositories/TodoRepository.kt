package com.example.todolist.data.repositories

import com.example.todolist.data.network.model.Todo
import com.example.todolist.data.network.model.Response
import com.example.todolist.data.network.model.User
import com.example.todolist.util.Constants.Companion.TODOS
import com.example.todolist.util.Constants.Companion.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoRepository @Inject constructor(
    db: FirebaseFirestore,
    auth: FirebaseAuth,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val todoCollection = db.collection(TODOS)
    private val userCollection = db.collection(USERS)
    private val userId = auth.currentUser!!.uid

    fun getTodos() = callbackFlow {
        val snapshotListener = todoCollection.whereEqualTo("createdBy.userId", userId)
            .addSnapshotListener { snapshot, e ->
                val todoResponse = if (snapshot != null) {
                    val todos = snapshot.toObjects(Todo::class.java)
                    Response.Success(todos)
                } else {
                    Response.Error(e)
                }
                trySend(todoResponse)
            }
            awaitClose {
                snapshotListener.remove()
            }
    }

    suspend fun insertTodo(todoName: String) {
        val todoId = todoCollection.document().id
        withContext(defaultDispatcher) {
            val currentUser = userCollection.document(userId).get().await()
                .toObject(User::class.java)!!

            val todo = Todo(todoId, todoName, currentUser)
            todoCollection.document(todoId).set(todo).await()
        }
    }

    suspend fun updateTodo(todo: Todo) {
        withContext(defaultDispatcher) {
            todoCollection.document(todo.id).update(mapOf(
                "name" to todo.name,
                "done" to todo.done
            )).await()
        }
    }

    suspend fun deleteTodo(todoId: String) {
        withContext(defaultDispatcher) {
            todoCollection.document(todoId).delete().await()
        }
    }
}
