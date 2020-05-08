package com.imaec.wishplace.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.model.KeywordDTO
import com.imaec.wishplace.model.PlaceDTO
import com.imaec.wishplace.ui.adapter.KeywordAdapter
import com.imaec.wishplace.ui.adapter.SearchAdapter
import com.imaec.wishplace.ui.util.PlaceItemDecoration

@Suppress("UNCHECKED_CAST")
class SearchViewModel(context: Context) : BaseViewModel(context) {

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
    val liveListSearchItem = MutableLiveData<ArrayList<Any>>()

    private fun getKeyword() : ArrayList<KeywordDTO> {
        return dummyList
    }

    fun search(keyword: String) {
        liveListSearchItem.value = dummyList2 as ArrayList<Any>
    }
}