package com.imaec.wishplace.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.model.PlaceDTO
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.PlaceDao
import com.imaec.wishplace.room.entity.PlaceEntity
import com.imaec.wishplace.ui.adapter.ListAdapter
import com.imaec.wishplace.ui.util.PlaceItemDecoration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
class ListViewModel(context: Context) : BaseViewModel(context) {

    private val dao: PlaceDao by lazy { AppDatabase.getInstance(context).placeDao() }

    val liveCategory = MutableLiveData<String>()
    val gridLayoutManager = GridLayoutManager(context, 2)
    val itemDecoration = PlaceItemDecoration(context)
    val liveListItem = MutableLiveData<ArrayList<Any>>().set(ArrayList())

    init {
        adapter = ListAdapter()
    }

    fun getData(categoryId: Int) {
        select(categoryId) { listEntity ->
            val listPlace = ArrayList<Any>()
            listEntity
                .sortedByDescending { it.saveTime }
                .groupBy { it.category }
                .forEach {
                    it.value.forEach { entity ->
                        listPlace.add(entity)
                    }
                }

            viewModelScope.launch {
                liveListItem.value = listPlace
            }
        }
    }

    private fun select(categoryId: Int, callback: (List<PlaceEntity>) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                callback(dao.selectByCategory(categoryId))
            }
        }
    }
}