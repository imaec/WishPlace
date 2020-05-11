package com.imaec.wishplace.base

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imaec.wishplace.model.PlaceDTO
import com.imaec.wishplace.room.entity.PlaceEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseViewModel(protected val context: Context) : ViewModel() {

    protected val TAG = this::class.java.simpleName
    private val job = Job()
    protected val viewModelScope = CoroutineScope(Dispatchers.Main + job)
    lateinit var adapter: BaseAdapter

    open fun onClick(view: View) {

    }

    fun addOnClickListener(onClick: (Any) -> Unit) {
        adapter.addOnClickListener(onClick)
    }

    fun addOnLongClickListener(onLongClick: (PlaceEntity) -> Unit) {
        adapter.addOnLongClickListener(onLongClick)
    }

    fun <T : Any> MutableLiveData<T>.set(value: T) : MutableLiveData<T> {
        this.value = value
        return this
    }

    fun MutableLiveData<String>.set(value: String) : MutableLiveData<String> {
        this.value = value
        return this
    }
}