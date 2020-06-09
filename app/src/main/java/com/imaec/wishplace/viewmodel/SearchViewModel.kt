package com.imaec.wishplace.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.repository.KeywordRepository
import com.imaec.wishplace.room.entity.KeywordEntity
import com.imaec.wishplace.ui.adapter.KeywordAdapter
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class SearchViewModel(
    private val keywordRepository: KeywordRepository
) : BaseViewModel() {

    init {
        adapter = KeywordAdapter()
    }

    private val _listKeywordItem = MutableLiveData<ArrayList<Any>>().set(ArrayList())
    val listKeywordItem: LiveData<ArrayList<Any>>
        get() = _listKeywordItem

    fun getKeyword() {
        viewModelScope.launch {
            keywordRepository.getList { listKeyword ->
                val listTemp = ArrayList<Any>()
                listKeyword
                    .sortedByDescending { it.saveTime }
                    .forEach { listTemp.add(it) }

                launch { _listKeywordItem.value = listTemp }
            }
        }
    }

    fun saveKeyword(entity: KeywordEntity) {
        viewModelScope.launch {
            keywordRepository.insert(entity)
        }
    }

    fun delete(entity: KeywordEntity, callback: () -> Unit) {
        viewModelScope.launch {
            keywordRepository.delete(entity)
            callback()
        }
    }

    fun deleteAll(callback: () -> Unit) {
        _listKeywordItem.value?.let { listKeyword ->
            viewModelScope.launch {
                keywordRepository.delete(*(listKeyword as ArrayList<KeywordEntity>).toTypedArray())
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