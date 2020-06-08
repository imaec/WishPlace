package com.imaec.wishplace.repository

import com.imaec.wishplace.room.dao.KeywordDao
import com.imaec.wishplace.room.entity.KeywordEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KeywordRepository(
    private val keywordDao: KeywordDao
) {

    suspend fun insert(entity: KeywordEntity) {
        withContext(Dispatchers.IO) {
            keywordDao.insert(entity)
        }
    }

    suspend fun getList(callback: (List<KeywordEntity>) -> Unit) {
        withContext(Dispatchers.IO) {
            callback(keywordDao.select())
        }
    }

    suspend fun delete(vararg entity: KeywordEntity) {
        withContext(Dispatchers.IO) {
            keywordDao.delete(*entity)
        }
    }

    companion object {
        @Volatile
        private var instance: KeywordRepository? = null

        fun getInstance(keywordDao: KeywordDao) =
            instance ?: synchronized(this) {
                instance ?: KeywordRepository(keywordDao).also { instance = it }
            }
    }
}