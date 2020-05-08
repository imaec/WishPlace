package com.imaec.wishplace.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imaec.wishplace.base.BaseAdapter
import com.imaec.wishplace.databinding.ItemItemBinding
import com.imaec.wishplace.model.PlaceDTO

class ListAdapter : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHolder(binding as ItemItemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.onBind(listItem[position] as PlaceDTO)
        }
    }

    inner class ItemViewHolder(private val binding: ItemItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: PlaceDTO) {
            binding.item = item
            binding.root.setOnClickListener {
                onClick(item)
            }
            binding.root.setOnLongClickListener {
                onLongClick(item)
                true
            }
        }
    }

    fun addOnClickListener(onClick: (Any) -> Unit) {
        this.onClick = onClick
    }

    fun addOnLongClickListener(onLongClick: (PlaceDTO) -> Unit) {
        this.onLongClick = onLongClick
    }
}