package com.example.todolist.data.repositories

import com.example.todolist.domain.model.Todo
import com.example.todolist.domain.model.Response
import com.example.todolist.domain.model.User
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
        val todoCollection = db.collection(TODOS)
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
        withContext(defaultDispatcher) {
            val todoCollection = db.collection(TODOS)
            val userCollection = db.collection(USERS)
            val userId = auth.currentUser!!.uid
            val todoId = todoCollection.document().id

            userCollection.document(userId).get().addOnSuccessListener { document ->
                if (document != null) {
                    val currentUser = document.toObject<User>()
                    val todo = Todo(todoId, todoName, currentUser!!)
                    todoCollection.document(todoId).set(todo)
                }
            }

//            val currentUser = userCollection.document(userId).get().await().toObject<User>()
//            val todo = Todo(todoId, todoName, currentUser!!)
//            todoCollection.document(todoId).set(todo).await()
        }
    }

    suspend fun updateTodo(todo: Todo) {
        withContext(defaultDispatcher) {
            val todoCollection = db.collection(TODOS)
            todoCollection.document(todo.id).update(mapOf(
                "name" to todo.name,
                "done" to todo.done
            )).await()
        }
    }

    suspend fun deleteTodo(todoId: String) {
        withContext(defaultDispatcher) {
            val todoCollection = db.collection(TODOS)
            todoCollection.document(todoId).delete().await()
        }
    }
}
