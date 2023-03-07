package com.example.todolist.data.repositories

import com.example.todolist.data.network.model.Todo
import com.example.todolist.data.network.model.Response
import com.example.todolist.data.network.model.User
import com.example.todolist.util.Constants.Companion.TODOS
import com.example.todolist.util.Constants.Companion.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val defaultDispatcher: CoroutineDispatcher
) {

    fun getTodos() = callbackFlow {
        val userId = auth.currentUser!!.uid
        val userTodos = db.collection(TODOS).whereEqualTo("createdBy.userId", userId)
        val snapshotListener = userTodos.addSnapshotListener { snapshot, e ->
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
        val userId = auth.currentUser!!.uid
        val todoId = db.collection(TODOS).document().id
        val user = db.collection(USERS).document(userId)
        val todoDocument = db.collection(TODOS).document(todoId)

        withContext(defaultDispatcher) {
            val currentUser = user.get().await().toObject<User>()
            val todo = Todo(todoId, todoName, currentUser!!)
            todoDocument.set(todo).await()
        }
    }

    suspend fun updateTodo(todo: Todo) {
        val todoDocument = db.collection(TODOS).document(todo.id)
        withContext(defaultDispatcher) {
            todoDocument.update(
                mapOf(
                    "name" to todo.name,
                    "done" to todo.done
                )
            ).await()
        }
    }

    suspend fun deleteTodo(todoId: String) {
        val todoDocument = db.collection(TODOS).document(todoId)
        withContext(defaultDispatcher) {
            todoDocument.delete().await()
        }
    }
}