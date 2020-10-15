package com.imaec.wishplace.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imaec.wishplace.base.BaseAdapter
import com.imaec.wishplace.databinding.ItemItemBinding
import com.imaec.wishplace.model.PlaceDTO

class RecommendAdapter : BaseAdapter() {

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