package com.imaec.wishplace.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imaec.wishplace.base.BaseAdapter
import com.imaec.wishplace.databinding.ItemLicenseBinding

class LicenseAdapter : BaseAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemLicenseBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHolder(binding as ItemLicenseBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.onBind(listItem[position] as String)
        }
    }

    inner class ItemViewHolder(private val binding: ItemLicenseBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: String) {
            binding.apply {
                this.item = item
            }
        }
    }
}