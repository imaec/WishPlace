package com.imaec.wishplace.base

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imaec.wishplace.room.entity.PlaceEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

abstract class BaseViewModel : ViewModel() {

    protected val TAG = this::class.java.simpleName
    private val job = Job()
    protected val viewModelScope = CoroutineScope(Dispatchers.Main + job)
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

    open fun addOnLongClickListener(onLongClick: (PlaceEntity) -> Unit) {
        adapter.addOnLongClickListener(onLongClick)
    }

    fun <T : Any> MutableLiveData<T>.set(value: T) : MutableLiveData<T> {
        this.value = value
        return this
    }
}