package com.example.todoapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.todoapp.R
import com.example.todoapp.data.TodoData
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fb = FirebaseFirestore.getInstance()
        fb.collection("table").document("w8d1PAO6rDD5ZIuombLU").get().addOnSuccessListener {
            Log.d("TTTT", "This data ${it.toObject(TodoData::class.java)}")

        }.addOnFailureListener {
         Log.d("TTTT", "Error")
        }
    }

}