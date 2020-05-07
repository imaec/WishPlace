package com.imaec.wishplace.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.model.PlaceDTO
import com.imaec.wishplace.ui.adapter.HomeAdapter
import com.imaec.wishplace.ui.util.HomeItemDecoration
import com.tmonet.samplewp.TYPE_CATEGORY
import com.tmonet.samplewp.TYPE_ITEM

class HomeViewModel(context: Context) : BaseViewModel(context) {

    private val dummyList = arrayListOf(
        "카페",
        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
        "카페",
        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
        "카페",
        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
        PlaceDTO("아이사구아", "서울시 은평구", "", "")
    )

    val adapter = HomeAdapter()
    val gridLayoutManager = getLayoutManager()
    val itemDecoration = HomeItemDecoration(context)
    val liveListItem = MutableLiveData<ArrayList<Any>>().set(getData())

    fun addOnClickListener(onClick: (PlaceDTO) -> Unit) {
        adapter.addOnClickListener(onClick)
    }

    fun addOnLongClickListener(onLongClick: (PlaceDTO) -> Unit) {
        adapter.addOnLongClickListener(onLongClick)
    }

    private fun getData() : ArrayList<Any> {
        return dummyList
    }

    private fun getLayoutManager() : GridLayoutManager {
        return GridLayoutManager(context, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (adapter.getItemViewType(position)) {
                        TYPE_CATEGORY -> 2
                        TYPE_ITEM -> 1
                        else -> 1
                    }
                }
            }
        }
    }
}