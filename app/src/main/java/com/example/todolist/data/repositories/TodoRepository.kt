package com.example.todolist.data.repositories

import com.example.todolist.data.network.Todo
import com.example.todolist.data.network.User
import com.example.todolist.util.Constants.Companion.TODOS
import com.example.todolist.util.Constants.Companion.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoRepository @Inject constructor(
    db: FirebaseFirestore,
    auth: FirebaseAuth
) {
    private val todoCollection = db.collection(TODOS)
    private val userCollection = db.collection(USERS)
    private val userId = auth.currentUser!!.uid

    val todos: Flow<List<Todo>> = flow {
        val todos = todoCollection.whereEqualTo("createdBy.userId", userId).get().await()
            .toObjects(Todo::class.java)
        emit(todos)
    }.flowOn(Dispatchers.IO)

    suspend fun insertTodo(todoName: String) {
        val todoId = todoCollection.document().id
        withContext(Dispatchers.IO) {
            val currentUser = userCollection.document(userId).get().await()
                .toObject(User::class.java)!!

            val todo = Todo(todoId, todoName, currentUser)
            todoCollection.document(todoId).set(todo).await()
        }
    }

    suspend fun updateTodo(todo: Todo) {
        withContext(Dispatchers.IO){
            todoCollection.document(todo.id).update(mapOf(
                "name" to todo.name,
                "done" to todo.done
            )).await()
        }
    }

    suspend fun deleteTodo(todoId: String) {
        withContext(Dispatchers.IO){
            todoCollection.document(todoId).delete().await()
        }
    }
}
