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
import com.imaec.wishplace.room.entity.CategoryEntity
import com.imaec.wishplace.room.entity.PlaceEntity
import com.imaec.wishplace.ui.util.HomeItemDecoration
import com.imaec.wishplace.ui.util.PlaceItemDecoration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(context: Context) : BaseViewModel(context) {

    init {
        adapter = HomeAdapter()
    }

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

    fun getData() {
        select { listEntity ->
            val listPlace = ArrayList<Any>()
            listEntity
                .sortedByDescending { it.saveTime }
                .groupBy { it.category }
                .forEach {
                    listPlace.add(CategoryEntity(it.value[0].foreignId, it.key))
                    it.value.forEach { entity ->
                        listPlace.add(entity)
                    }
                }

            viewModelScope.launch {
                liveListItem.value = listPlace
            }
        }
    }

    fun delete(entity: PlaceEntity, callback: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.delete(entity)
            }
            callback()
        }
    }

    private fun select(callback: (List<PlaceEntity>) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                callback(dao.select())
            }
        }
    }
}