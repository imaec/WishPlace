package com.imaec.wishplace.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.model.PlaceDTO
import com.imaec.wishplace.ui.adapter.HomeAdapter
import com.imaec.wishplace.TYPE_CATEGORY
import com.imaec.wishplace.TYPE_ITEM
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.CategoryDao
import com.imaec.wishplace.room.dao.PlaceDao
import com.imaec.wishplace.room.entity.PlaceEntity
import com.imaec.wishplace.ui.util.HomeItemDecoration
import com.imaec.wishplace.ui.util.PlaceItemDecoration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(context: Context) : BaseViewModel(context) {

//    private val dummyList = arrayListOf(
//        "카페",
//        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
//        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
//        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
//        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
//        "카페",
//        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
//        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
//        "카페",
//        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
//        PlaceDTO("아이사구아", "서울시 은평구", "", ""),
//        PlaceDTO("아이사구아", "서울시 은평구", "", "")
//    )
    private val categoryDao: CategoryDao by lazy { AppDatabase.getInstance(context).categoryDao() }
    private val dao: PlaceDao by lazy { AppDatabase.getInstance(context).placeDao() }
    val gridLayoutManager = GridLayoutManager(context, 2).apply {
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
    val itemDecoration = HomeItemDecoration(context)
    val liveListItem = MutableLiveData<ArrayList<Any>>().set(ArrayList())

    init {
        adapter = HomeAdapter()
    }

    fun getData() {
        select { listEntity ->
            val listPlace = ArrayList<Any>()
            listEntity
                .sortedBy { it.category }
                .groupBy { it.category }
                .forEach {
                    listPlace.add(it.key)
                    it.value.forEach { entity ->
                        listPlace.add(entity)
                    }
                }

            viewModelScope.launch {
                liveListItem.value = listPlace
            }
        }
    }

    fun selectCount(callback: (Int) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val count = dao.selectCount()
                callback(count)
            }
        }
    }

    fun select(callback: (List<PlaceEntity>) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                callback(dao.select())
            }
        }
    }
}