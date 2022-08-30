package com.example.listmaker.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listmaker.databinding.RecyclerviewHolderBinding
import com.example.listmaker.models.TaskList

class RecyclerViewAdapter(private val lists: MutableList<TaskList>, val listener: RecyclerViewClickListener) :
    RecyclerView.Adapter<RecyclerviewHolder>() {

    interface RecyclerViewClickListener{
        fun listItemClicked(list: TaskList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerviewHolder {
        val binding =
            RecyclerviewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerviewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerviewHolder, position: Int) {
        holder.binding.itemNumber.text = (position + 1).toString()
        holder.binding.item.text = lists[position].name
        holder.itemView.setOnClickListener{ listener.listItemClicked(lists[position])}
    }

    override fun getItemCount(): Int {
        return lists.size
    }
}
