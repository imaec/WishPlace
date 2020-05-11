package com.imaec.wishplace.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imaec.wishplace.base.BaseAdapter
import com.imaec.wishplace.databinding.ItemCategorySelectBinding
import com.imaec.wishplace.room.entity.CategoryEntity

class CategoryAdapter : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemCategorySelectBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHolder(binding as ItemCategorySelectBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.onBind(listItem[position] as CategoryEntity)
        }
    }

    inner class ItemViewHolder(private val binding: ItemCategorySelectBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: CategoryEntity) {
            binding.category = item
            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }
}