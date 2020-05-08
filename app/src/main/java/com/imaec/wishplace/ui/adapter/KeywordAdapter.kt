package com.imaec.wishplace.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imaec.wishplace.TYPE_FOOTER
import com.imaec.wishplace.base.BaseAdapter
import com.imaec.wishplace.model.KeywordDTO
import com.imaec.wishplace.TYPE_ITEM
import com.imaec.wishplace.databinding.FooterDeleteBinding
import com.imaec.wishplace.databinding.ItemKeywordBinding

class KeywordAdapter : BaseAdapter() {

    override fun getItemViewType(position: Int): Int = if (listItem.size > 0 && position == listItem.size) { TYPE_FOOTER } else { TYPE_ITEM }

    override fun getItemCount(): Int = if (listItem.size > 0) super.getItemCount() + 1 else super.getItemCount()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_FOOTER) {
            binding = FooterDeleteBinding.inflate(LayoutInflater.from(parent.context))
            FooterViewHolder(binding as FooterDeleteBinding)
        } else { // TYPE_ITEM
            binding = ItemKeywordBinding.inflate(LayoutInflater.from(parent.context))
            ItemViewHolder(binding as ItemKeywordBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.onBind(listItem[position] as KeywordDTO)
        }
    }

    inner class ItemViewHolder(private val binding: ItemKeywordBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: KeywordDTO) {
            binding.item = item
        }
    }

    inner class FooterViewHolder(private val binding: FooterDeleteBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: String) {

        }
    }
}