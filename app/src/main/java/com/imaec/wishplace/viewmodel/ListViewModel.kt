package com.imaec.wishplace.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.model.PlaceDTO
import com.imaec.wishplace.ui.adapter.ListAdapter
import com.imaec.wishplace.ui.util.PlaceItemDecoration

@Suppress("UNCHECKED_CAST")
class ListViewModel(context: Context) : BaseViewModel(context) {

    val liveCategory = MutableLiveData<String>()

    private val dummyList = arrayListOf(
        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
        PlaceDTO("아이사구아", "서울시 은평구", "", "")
    )

    val adapter = ListAdapter()
    val gridLayoutManager = GridLayoutManager(context, 2)
    val itemDecoration = PlaceItemDecoration(context)
    val liveListItem = MutableLiveData<ArrayList<Any>>().set(getData())

    private fun getData() : ArrayList<Any> {
        return dummyList as ArrayList<Any>
    }

    fun addOnClickListener(onClick: (Any) -> Unit) {
        adapter.addOnClickListener(onClick)
    }

    fun addOnLongClickListener(onLongClick: (PlaceDTO) -> Unit) {
        adapter.addOnLongClickListener(onLongClick)
    }
}