package com.imaec.wishplace.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.imaec.wishplace.TYPE_CATEGORY
import com.imaec.wishplace.TYPE_CATEGORY_EDIT
import com.imaec.wishplace.base.BaseAdapter
import com.imaec.wishplace.databinding.ItemCategoryEditBinding
import com.imaec.wishplace.databinding.ItemCategorySelectBinding
import com.imaec.wishplace.room.entity.CategoryEntity

class CategoryAdapter(private val viewType: Int) : BaseAdapter() {

    private lateinit var onDeleteClick: (CategoryEntity) -> Unit

    override fun getItemViewType(position: Int): Int = viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = if (viewType == TYPE_CATEGORY_EDIT)
            ItemCategoryEditBinding.inflate(LayoutInflater.from(parent.context))
        else
            ItemCategorySelectBinding.inflate(LayoutInflater.from(parent.context))
        return if (viewType == TYPE_CATEGORY_EDIT)
            ItemViewHolder(binding as ItemCategoryEditBinding)
        else
            ItemViewHolder(binding as ItemCategorySelectBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.onBind(listItem[position] as CategoryEntity)
        }
    }

    inner class ItemViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: CategoryEntity) {
            if (binding is ItemCategoryEditBinding) {
                binding.apply {
                    category = item
                    root.setOnClickListener {
                        onClick(item)
                    }
                    imageItemDelete.setOnClickListener {
                        onDeleteClick(item)
                    }
                }
            } else if (binding is ItemCategorySelectBinding) {
                binding.apply {
                    category = item
                    root.setOnClickListener {
                        onClick(item)
                    }
                }
            }
        }
    }

    fun addOnDeleteClickListener(onClick: (CategoryEntity) -> Unit) {
        this.onDeleteClick = onClick
    }
}