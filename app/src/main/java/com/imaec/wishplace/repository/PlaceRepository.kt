package com.imaec.wishplace.repository

import com.imaec.wishplace.room.dao.PlaceDao
import com.imaec.wishplace.room.entity.PlaceEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaceRepository(
    private val dao: PlaceDao
) {

    suspend fun insert(entity: PlaceEntity) {
        withContext(Dispatchers.IO) {
            dao.insert(entity)
        }
    }

    suspend fun getCount(callback: (Int) -> Unit) {
        withContext(Dispatchers.IO) {
            callback(dao.selectCount())
        }
    }

    suspend fun getList(callback: (List<PlaceEntity>) -> Unit) {
        withContext(Dispatchers.IO) {
            callback(dao.select())
        }
    }

    suspend fun getPlace(placeId: Int, callback: (PlaceEntity) -> Unit) {
        withContext(Dispatchers.IO) {
            callback(dao.select(placeId))
        }
    }

    suspend fun getListByCategory(categoryId: Int, callback: (List<PlaceEntity>) -> Unit) {
        withContext(Dispatchers.IO) {
            callback(dao.selectByCategory(categoryId))
        }
    }

    suspend fun getListByName(name: String, callback: (List<PlaceEntity>) -> Unit) {
        withContext(Dispatchers.IO) {
            callback(dao.selectByName(name))
        }
    }

    suspend fun getListByAddress(address: String, callback: (List<PlaceEntity>) -> Unit) {
        withContext(Dispatchers.IO) {
            callback(dao.selectByAddress(address))
        }
    }

    suspend fun update(vararg entity: PlaceEntity, callback: (Int) -> Unit) {
        withContext(Dispatchers.IO) {
            callback(dao.update(*entity))
        }
    }

    suspend fun delete(entity: PlaceEntity) {
        withContext(Dispatchers.IO) {
            dao.delete(entity)
        }
    }

    companion object {
        @Volatile
        private var instance: PlaceRepository? = null

        fun getInstance(placeDao: PlaceDao) =
            instance ?: synchronized(this) {
                instance ?: PlaceRepository(placeDao).also { instance = it }
            }
    }
}