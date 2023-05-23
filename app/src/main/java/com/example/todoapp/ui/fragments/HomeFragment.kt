package com.example.todoapp.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mobiletodoapp.utils.hideKeyboard
import com.example.todoapp.R
import com.example.todoapp.data.TodoData
import com.example.todoapp.databinding.FragmentHomeBinding
import com.example.todoapp.presentation.MainViewModel
import com.example.todoapp.ui.adapter.TodoAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val adapter by lazy { TodoAdapter() }
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        initVariables()
        initListeners()
        initObservers()

    }


    private fun initListeners() {

        adapter.setOnItemClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToEditFragment(
                    it.id, it.title.toString(), it.body.toString()
                )
            )
        }

        binding.searchView.doAfterTextChanged {
            if (binding.searchView.text.toString().isEmpty()) {
                hideKeyboard()
                lifecycleScope.launch {
                    viewModel.getAllTodo()
                }
            } else {
                lifecycleScope.launch {
                    viewModel.searchTodo(binding.searchView.text.toString())
                }
            }
        }

        binding.btnAdd.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddFragment())
        }

    }


    private fun initObservers() {
        viewModel.getAllNotes.observe(requireActivity()) {
            adapter.models = it as MutableList<TodoData>
        }
    }


    private fun initVariables() {
        binding.recyclerView.adapter = adapter
        lifecycleScope.launch {
            viewModel.getAllTodo()
            viewModel.getAllNotes.observe(viewLifecycleOwner) { todoList ->
                adapter.models = todoList as MutableList<TodoData>
            }
        }

        val swapHelper = getSwipeManager()
        swapHelper.attachToRecyclerView(binding.recyclerView)
    }


    private fun getSwipeManager(): ItemTouchHelper {
        return ItemTouchHelper(object: ItemTouchHelper
        .SimpleCallback(0,ItemTouchHelper.LEFT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            @SuppressLint("ResourceAsColor")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val todoData: TodoData = adapter.models[position]

                lifecycleScope.launch {
                    viewModel.deleteTodo(todoData)
                }
                adapter.models.removeAt(position)
                adapter.notifyItemRemoved(position)

                Snackbar.make(
                    viewHolder.itemView,
                    "Deleted", Snackbar.LENGTH_SHORT
                ).apply {
                    setAction("Cancel") {
                        lifecycleScope.launch {
                            viewModel.addTodo(todoData)
                        }
                        adapter.models.add(position, todoData)
                        adapter.notifyItemInserted(position)
                        binding.recyclerView.scrollToPosition(position)
                    }
                    setActionTextColor(Color.WHITE)
                }.show()
            }
        })

    }
}