package com.imaec.wishplace.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imaec.wishplace.TYPE_CATEGORY
import com.imaec.wishplace.TYPE_ITEM
import com.imaec.wishplace.base.BaseAdapter
import com.imaec.wishplace.databinding.ItemCategoryBinding
import com.imaec.wishplace.databinding.ItemItemBinding
import com.imaec.wishplace.room.entity.CategoryEntity
import com.imaec.wishplace.room.entity.PlaceEntity

class HomeAdapter : BaseAdapter() {

    override fun getItemViewType(position: Int): Int = if (listItem[position] is CategoryEntity) TYPE_CATEGORY else TYPE_ITEM

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
            holder.onBind(listItem[position] as CategoryEntity)
        } else if (holder is ItemViewHolder) {
            holder.onBind(listItem[position] as PlaceEntity)
        }
    }

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: CategoryEntity) {
            binding.apply {
                this.item = item
                root.setOnClickListener {
                    onClickWithTransition(item, root)
                }
            }
        }
    }

    inner class ItemViewHolder(private val binding: ItemItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: PlaceEntity) {
            binding.apply {
                this.item = item
                root.setOnClickListener {
                    onClickWithTransition(item, imageItemThumb)
                }
                root.setOnLongClickListener {
                    onLongClick(item)
                    true
                }
            }
        }
    }
}