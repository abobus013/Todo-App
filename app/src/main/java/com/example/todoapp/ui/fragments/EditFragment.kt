package com.example.todoapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mobiletodoapp.utils.showSnackbar
import com.example.todoapp.R
import com.example.todoapp.data.TodoData
import com.example.todoapp.databinding.FragmentEditBinding
import com.example.todoapp.presentation.MainViewModel
import kotlinx.coroutines.launch

class EditFragment : Fragment(R.layout.fragment_edit) {
    private lateinit var binding: FragmentEditBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var etTitle: EditText
    private lateinit var etBody: EditText

    private val args: EditFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditBinding.bind(view)

        etTitle = binding.etTitle
        etBody = binding.etBody

        etTitle.setText(args.title)
        etBody.setText(args.body)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.bgActionBar.setOnClickListener {

            val title = binding.etTitle.text.toString()
            val body = binding.etBody.text.toString()

            if (title.isNotEmpty() && body.isNotEmpty()) {
                lifecycleScope.launch {
                    viewModel.updateTodo(TodoData(args.id, title, body))
                }
                showSnackbar("Successfully updated")
                findNavController().popBackStack()
            } else {
                showSnackbar("Please, fill all fields!")
            }
        }

    }
}