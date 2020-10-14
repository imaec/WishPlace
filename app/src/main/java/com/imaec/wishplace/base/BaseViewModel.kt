package com.imaec.wishplace.base

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imaec.wishplace.model.PlaceDTO
import com.imaec.wishplace.room.entity.PlaceEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

abstract class BaseViewModel : ViewModel() {

    protected val TAG = this::class.java.simpleName
    lateinit var adapter: BaseAdapter

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }

    open fun onClick(view: View) {

    }

    open fun addOnClickListener(onClick: (Any) -> Unit) {
        adapter.addOnClickListener(onClick)
    }

    open fun addOnClickListener(onClick: (Any, View) -> Unit) {
        adapter.addOnClickListener(onClick)
    }

    open fun addOnLongClickListener(onLongClick: (PlaceDTO) -> Unit) {
        adapter.addOnLongClickListener(onLongClick)
    }
}