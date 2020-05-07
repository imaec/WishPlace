package com.imaec.wishplace.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imaec.wishplace.base.BaseAdapter
import com.imaec.wishplace.databinding.ItemCategoryBinding
import com.imaec.wishplace.databinding.ItemItemBinding
import com.imaec.wishplace.model.PlaceDTO
import com.tmonet.samplewp.*

class HomeAdapter : BaseAdapter() {

    override fun getItemViewType(position: Int): Int = if (position % 5 == 0) TYPE_CATEGORY else TYPE_ITEM

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_CATEGORY) {
            binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context))
            CategoryViewHolder(binding as ItemCategoryBinding)
        } else { // TYPE_ITEM
            binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context))
            ItemViewHolder(binding as ItemItemBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CategoryViewHolder) {
            holder.onBind(listItem[position] as String)
        } else if (holder is ItemViewHolder) {
            holder.onBind(listItem[position] as PlaceDTO)
        }
    }

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: String) {
            binding.item = item
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

    fun addOnClickListener(onClick: (PlaceDTO) -> Unit) {
        this.onClick = onClick
    }

    fun addOnLongClickListener(onLongClick: (PlaceDTO) -> Unit) {
        this.onLongClick = onLongClick
    }
}