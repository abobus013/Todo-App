package com.example.todoapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.TodoData
import com.example.todoapp.databinding.ItemLayoutBinding

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private var onItemClickListener: ((TodoData) -> Unit)? = null

    fun setOnItemClickListener(block: (TodoData) -> Unit) {
        onItemClickListener = block
    }

    inner class TodoViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            val d = models[adapterPosition]
            val pos = adapterPosition + 1
            binding.tvTitle.text = d.title
            binding.tvBody.text = d.body
            binding.tvPosition.text = pos.toString()
        }
    }

    var models = mutableListOf<TodoData>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TodoViewHolder(
        ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = models.size

    override fun onBindViewHolder(holder: TodoAdapter.TodoViewHolder, position: Int) {
        holder.bind()
    }

    fun removeItem(todoData: TodoData) {
        val index = models.indexOf(todoData)
        if (index != -1) {
            models = models.toMutableList().apply { removeAt(index) }
            notifyItemRemoved(index)
        }
    }
}