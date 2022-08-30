package com.example.listmaker.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listmaker.databinding.ListItemsViewHolderBinding
import com.example.listmaker.models.TaskList

class ListItemsAdapter(var list: TaskList) : RecyclerView.Adapter<ListItemsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemsViewHolder {
        val binding =
            ListItemsViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemsViewHolder, position: Int) {
        holder.binding.item.text = list.tasks[position]
    }

    override fun getItemCount(): Int {
        return list.tasks.size
    }
}