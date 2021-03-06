package com.imaec.wishplace.base

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.imaec.wishplace.model.PlaceDTO
import com.imaec.wishplace.room.entity.PlaceEntity

abstract class BaseAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected val TAG = this::class.java.simpleName

    protected lateinit var binding: ViewDataBinding
    protected val listItem = ArrayList<Any>()
    protected lateinit var onClick: (Any) -> Unit
    protected lateinit var onClickWithTransition: (Any, View) -> Unit
    protected lateinit var onLongClick: (PlaceDTO) -> Unit

    override fun getItemCount(): Int = listItem.size

    fun addItems(list: ArrayList<Any>) {
        listItem.addAll(list)
    }

    fun clearItem() {
        listItem.clear()
        notifyDataSetChanged()
    }

    fun addOnClickListener(onClick: (Any) -> Unit) {
        this.onClick = onClick
    }

    fun addOnClickListener(onClickWithTransition: (Any, View) -> Unit) {
        this.onClickWithTransition = onClickWithTransition
    }

    fun addOnLongClickListener(onLongClick: (PlaceDTO) -> Unit) {
        this.onLongClick = onLongClick
    }
}