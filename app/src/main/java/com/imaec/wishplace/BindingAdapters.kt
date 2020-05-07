package com.imaec.wishplace

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tmonet.samplewp.TYPE_CATEGORY
import com.tmonet.samplewp.TYPE_ITEM
import com.imaec.wishplace.base.BaseAdapter
import com.imaec.wishplace.ui.adapter.HomeAdapter

object BindingAdapters {

    private val TAG = this::class.java.simpleName

    @JvmStatic
    @BindingAdapter("adapter")
    fun setAdapter(recyclerView: RecyclerView, adapter: HomeAdapter) {
        recyclerView.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("layoutManager")
    fun setLayoutManager(recyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager) {
        recyclerView.layoutManager = layoutManager
    }

    @JvmStatic
    @BindingAdapter("itemDecoration")
    fun setItemDecoration(recyclerView: RecyclerView, itemDecoration: RecyclerView.ItemDecoration) {
        recyclerView.addItemDecoration(itemDecoration)
    }

    @JvmStatic
    @BindingAdapter("items")
    fun setItems(recyclerView: RecyclerView, items: ArrayList<Any>) {
        (recyclerView.adapter as BaseAdapter).apply {
            addItems(items)
            notifyDataSetChanged()
        }
    }
}