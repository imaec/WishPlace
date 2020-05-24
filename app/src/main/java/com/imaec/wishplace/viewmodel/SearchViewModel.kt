package com.imaec.wishplace.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.model.KeywordDTO
import com.imaec.wishplace.model.PlaceDTO
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.PlaceDao
import com.imaec.wishplace.room.entity.PlaceEntity
import com.imaec.wishplace.ui.adapter.KeywordAdapter
import com.imaec.wishplace.ui.adapter.SearchAdapter
import com.imaec.wishplace.ui.util.PlaceItemDecoration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
class SearchViewModel(context: Context) : BaseViewModel(context) {

    private val dao: PlaceDao by lazy { AppDatabase.getInstance(context).placeDao() }

    private val dummyList = arrayListOf(
        KeywordDTO("검색어1", "2020.05.08"),
        KeywordDTO("검색어2", "2020.05.08"),
        KeywordDTO("검색어3", "2020.05.08"),
        KeywordDTO("검색어4", "2020.05.08"),
        KeywordDTO("검색어5", "2020.05.08"),
        KeywordDTO("검색어6", "2020.05.08"),
        KeywordDTO("검색어7", "2020.05.08"),
        KeywordDTO("검색어1", "2020.05.08"),
        KeywordDTO("검색어2", "2020.05.08"),
        KeywordDTO("검색어3", "2020.05.08"),
        KeywordDTO("검색어4", "2020.05.08"),
        KeywordDTO("검색어5", "2020.05.08"),
        KeywordDTO("검색어6", "2020.05.08"),
        KeywordDTO("검색어7", "2020.05.08")
    )

    private val dummyList2 = arrayListOf(
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

    val keywordAdapter = KeywordAdapter()
    val searchAdapter = SearchAdapter()
    val linearLayoutManager = LinearLayoutManager(context)
    val gridLayoutManager = GridLayoutManager(context, 2)
    val itemDecoration = PlaceItemDecoration(context)
    val liveListKeywordItem = MutableLiveData<ArrayList<Any>>().set(getKeyword() as ArrayList<Any>)
    val liveListSearchItem = MutableLiveData<ArrayList<Any>>().set(ArrayList())

    private fun getKeyword() : ArrayList<KeywordDTO> {
        return dummyList
    }

    fun search(keyword: String, option: String, onFail: () -> Unit) {
        viewModelScope.launch {
            var listPlace = ArrayList<Any>()
            withContext(Dispatchers.IO) {
                when (option) {
                    "이름" -> listPlace = dao.selectByName("%$keyword%") as ArrayList<Any>
                    "주소" -> listPlace = dao.selectByAddress("%$keyword%") as ArrayList<Any>
                    else -> onFail()
                }
            }
            liveListSearchItem.value = listPlace
        }
    }
}