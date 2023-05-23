package com.example.todoapp.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.TodoData
import com.example.todoapp.domain.MainRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val repo = MainRepository(FirebaseFirestore.getInstance())
    private val db = FirebaseFirestore.getInstance()
    private val _getAllNotes = MutableLiveData<List<TodoData>>()
    val getAllNotes: LiveData<List<TodoData>> get() = _getAllNotes

    fun getAllTodo() {
        viewModelScope.launch {
            repo.getAllTodo().collect { todoList ->
                _getAllNotes.value = todoList
            }
        }
    }

    suspend fun addTodo(todoData: TodoData) {
//        repo.addTodo(todoData)
        db.collection("table")
            .document()
            .set(todoData)
            .addOnSuccessListener {
                Log.d("TTTT", "Todo added with ID: $it")
            }
            .addOnFailureListener {
                Log.e("TTTT", "Error adding todo", it)
            }

    }

    suspend fun updateTodo(todoData: TodoData) {
        repo.updateTodo(todoData)
    }

    suspend fun deleteTodo(todoData: TodoData) {
        repo.deleteTodo(todoData)
        getAllTodo()
    }


    fun searchTodo(name: String) {
        viewModelScope.launch {
            repo.searchTodo(name).collect { todoList ->
                _getAllNotes.value = todoList
            }

        }
    }

}
