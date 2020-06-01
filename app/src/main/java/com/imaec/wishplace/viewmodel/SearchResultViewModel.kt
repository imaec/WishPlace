package com.imaec.wishplace.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.PlaceDao
import com.imaec.wishplace.room.entity.PlaceEntity
import com.imaec.wishplace.ui.adapter.SearchAdapter
import com.imaec.wishplace.ui.util.PlaceItemDecoration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
class SearchResultViewModel(context: Context) : BaseViewModel(context) {

    init {
        adapter = SearchAdapter()
    }

    private val dao: PlaceDao by lazy { AppDatabase.getInstance(context).placeDao() }

    val gridLayoutManager = GridLayoutManager(context, 2)
    val itemDecoration = PlaceItemDecoration(context)
    val liveListSearchItem = MutableLiveData<ArrayList<Any>>().set(ArrayList())

    fun search(keyword: String, option: String, callback: (Boolean) -> Unit) {
        (adapter as SearchAdapter).setKeyword(keyword)
        (adapter as SearchAdapter).setOption(option)

        viewModelScope.launch {
            var listPlace = ArrayList<Any>()
            withContext(Dispatchers.IO) {
                when (option) {
                    "이름" -> listPlace = dao.selectByName("%$keyword%") as ArrayList<Any>
                    "주소" -> listPlace = dao.selectByAddress("%$keyword%") as ArrayList<Any>
                    else -> callback(false)
                }
            }
            liveListSearchItem.value = listPlace
            callback(true)
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
}