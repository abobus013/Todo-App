package com.example.todoapp.domain

import com.example.todoapp.data.TodoData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class MainRepository(private val fb: FirebaseFirestore) {

    fun getAllTodo() = flow {
        emit(fb.collection("table").get().await().documents.mapNotNull {
            TodoData(it.id, it.data!!["title"].toString(), it.data!!["body"].toString())
        })

    }.catch {
        it.printStackTrace()
    }.flowOn(Dispatchers.IO)

    suspend fun deleteTodo(todoData: TodoData) {
        fb.collection("table").document(todoData.id).delete().await()
    }

    suspend fun addTodo(todoData: TodoData) {
        val data = mapOf(
            "title" to todoData.title,
            "body" to todoData.body
        )
        fb.collection("table").document().set(data).await()
    }

    suspend fun updateTodo(todoData: TodoData) {
        fb.collection("table").document(todoData.id).update(
            "title", todoData.title,"body",todoData.body
        ).await()
    }

    suspend fun searchTodo(name: String) = flow {
        emit(fb.collection("table").whereEqualTo("title", name).get().await().documents.mapNotNull {
            TodoData(it.id, it.data!!["title"].toString(), it.data!!["body"].toString())
        })
    }.catch {
        it.printStackTrace()
    }.flowOn(Dispatchers.IO)


}