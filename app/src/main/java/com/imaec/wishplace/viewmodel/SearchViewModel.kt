package com.imaec.wishplace.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.model.KeywordDTO
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.KeywordDao
import com.imaec.wishplace.room.dao.PlaceDao
import com.imaec.wishplace.room.entity.KeywordEntity
import com.imaec.wishplace.ui.adapter.KeywordAdapter
import com.imaec.wishplace.ui.adapter.SearchAdapter
import com.imaec.wishplace.ui.util.PlaceItemDecoration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
class SearchViewModel(context: Context) : BaseViewModel(context) {

    private val placeDao: PlaceDao by lazy { AppDatabase.getInstance(context).placeDao() }
    private val keywordDao: KeywordDao by lazy { AppDatabase.getInstance(context).keywordDao() }

    val keywordAdapter = KeywordAdapter()
    val searchAdapter = SearchAdapter()
    val linearLayoutManager = LinearLayoutManager(context)
    val gridLayoutManager = GridLayoutManager(context, 2)
    val itemDecoration = PlaceItemDecoration(context)
    val liveListKeywordItem = MutableLiveData<ArrayList<Any>>().set(ArrayList())
    val liveListSearchItem = MutableLiveData<ArrayList<Any>>().set(ArrayList())

    fun getKeyword() {
        viewModelScope.launch {
            var listKeyword = ArrayList<Any>()
            withContext(Dispatchers.IO) {
                listKeyword = keywordDao.select() as ArrayList<Any>
            }
            liveListKeywordItem.value = listKeyword
        }
    }

    fun saveKeyword(entity: KeywordEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                keywordDao.insert(entity)
            }
        }
    }

    fun search(keyword: String, option: String, onFail: () -> Unit) {
        viewModelScope.launch {
            var listPlace = ArrayList<Any>()
            withContext(Dispatchers.IO) {
                when (option) {
                    "이름" -> listPlace = placeDao.selectByName("%$keyword%") as ArrayList<Any>
                    "주소" -> listPlace = placeDao.selectByAddress("%$keyword%") as ArrayList<Any>
                    else -> onFail()
                }
            }
            liveListSearchItem.value = listPlace
        }
    }

    fun delete(entity: KeywordEntity, callback: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                keywordDao.delete(entity)
            }
            callback()
        }
    }

    fun deleteAll(callback: () -> Unit) {
        liveListKeywordItem.value?.let { listKeyword ->
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    keywordDao.delete(*(listKeyword as ArrayList<KeywordEntity>).toTypedArray())
                }
                callback()
            }
        }
    }

    override fun addOnClickListener(onClick: (Any) -> Unit) {
        keywordAdapter.addOnClickListener(onClick)
    }

    fun addOnDeleteClickListener(onClick: (KeywordEntity) -> Unit) {
        keywordAdapter.addOnDeleteClickListener(onClick)
    }

    fun addOnAllDeleteClickListener(onClick: () -> Unit) {
        keywordAdapter.addOnAllDeleteClickListener(onClick)
    }
}