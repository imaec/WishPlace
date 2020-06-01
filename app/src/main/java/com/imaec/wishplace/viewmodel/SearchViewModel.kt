package com.imaec.wishplace.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.KeywordDao
import com.imaec.wishplace.room.entity.KeywordEntity
import com.imaec.wishplace.ui.adapter.KeywordAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
class SearchViewModel(context: Context) : BaseViewModel(context) {

    init {
        adapter = KeywordAdapter()
    }

    private val dao: KeywordDao by lazy { AppDatabase.getInstance(context).keywordDao() }

    val linearLayoutManager = LinearLayoutManager(context)
    val liveListKeywordItem = MutableLiveData<ArrayList<Any>>().set(ArrayList())

    fun getKeyword() {
        viewModelScope.launch {
            val listKeyword = ArrayList<Any>()
            withContext(Dispatchers.IO) {
                val listTemp = dao.select()
                listTemp
                    .sortedByDescending { it.saveTime }
                    .forEach { listKeyword.add(it) }
            }
            liveListKeywordItem.value = listKeyword
        }
    }

    fun saveKeyword(entity: KeywordEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.insert(entity)
            }
        }
    }

    fun delete(entity: KeywordEntity, callback: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.delete(entity)
            }
            callback()
        }
    }

    fun deleteAll(callback: () -> Unit) {
        liveListKeywordItem.value?.let { listKeyword ->
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    dao.delete(*(listKeyword as ArrayList<KeywordEntity>).toTypedArray())
                }
                callback()
            }
        }
    }

    fun addOnDeleteClickListener(onClick: (KeywordEntity) -> Unit) {
        (adapter as KeywordAdapter).addOnDeleteClickListener(onClick)
    }

    fun addOnAllDeleteClickListener(onClick: () -> Unit) {
        (adapter as KeywordAdapter).addOnAllDeleteClickListener(onClick)
    }
}