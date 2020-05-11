package com.imaec.wishplace.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.imaec.wishplace.model.PlaceDTO
import com.imaec.wishplace.room.entity.PlaceEntity

abstract class BaseAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected val TAG = this::class.java.simpleName

    protected lateinit var binding: ViewDataBinding
    protected val listItem = ArrayList<Any>()
    protected lateinit var onClick: (Any) -> Unit
    protected lateinit var onLongClick: (PlaceEntity) -> Unit

    override fun getItemCount(): Int = listItem.size

    fun addItems(list: ArrayList<Any>) {
        listItem.addAll(list)
    }

    fun addItem(item: Any) {
        listItem.add(item)
    }

    fun clearItem() {
        listItem.clear()
    }

    fun getItem(position: Int) : Any {
        return listItem[position]
    }

    fun addOnClickListener(onClick: (Any) -> Unit) {
        this.onClick = onClick
    }

    fun addOnLongClickListener(onLongClick: (PlaceEntity) -> Unit) {
        this.onLongClick = onLongClick
    }
}