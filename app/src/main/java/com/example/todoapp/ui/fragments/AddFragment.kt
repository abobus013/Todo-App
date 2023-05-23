package com.example.todoapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mobiletodoapp.utils.showSnackbar
import com.example.todoapp.R
import com.example.todoapp.data.TodoData
import com.example.todoapp.databinding.FragmentAddBinding
import com.example.todoapp.presentation.MainViewModel
import kotlinx.coroutines.launch

class AddFragment : Fragment(R.layout.fragment_add) {
    private lateinit var binding: FragmentAddBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var etTitle: EditText
    private lateinit var etBody: EditText
    private lateinit var btnAdd: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddBinding.bind(view)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        etTitle = binding.etTitle
        etBody = binding.etBody
        btnAdd = binding.btnAdd

        btnAdd.setOnClickListener {

            Log.d("TTTT", "Btn add on")

            val title = etTitle.text.toString()
            val body = etBody.text.toString()

            lifecycleScope.launch {
                if (title.isNotEmpty() && body.isNotEmpty()) {
                    viewModel.addTodo(TodoData("", title, body))
                    showSnackbar("Successfully added ")
                    findNavController().popBackStack()
                } else {
                    showSnackbar("Please, fill all fields!")
                }
            }
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }
}