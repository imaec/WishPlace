package com.imaec.wishplace.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imaec.wishplace.base.BaseAdapter
import com.imaec.wishplace.databinding.ItemNaverPlaceBinding
import com.imaec.wishplace.model.NaverPlaceDTO

class NaverPlaceAdapter : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemNaverPlaceBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHolder(binding as ItemNaverPlaceBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.onBind(listItem[position] as NaverPlaceDTO.Item)
        }
    }

    inner class ItemViewHolder(private val binding: ItemNaverPlaceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: NaverPlaceDTO.Item) {
            binding.apply {
                this.item = item
                root.setOnClickListener { onClick(item) }
            }
        }
    }
}