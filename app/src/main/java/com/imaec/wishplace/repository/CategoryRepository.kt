package com.imaec.wishplace.repository

import com.imaec.wishplace.room.dao.CategoryDao
import com.imaec.wishplace.room.entity.CategoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepository(
    private val dao: CategoryDao
) {

    suspend fun insert(entity: CategoryEntity) {
        withContext(Dispatchers.IO) {
            dao.insert(entity)
        }
    }

    suspend fun getCount(callback: (Int) -> Unit) {
        withContext(Dispatchers.IO) {
            callback(dao.getCount())
        }
    }

    suspend fun getCount(category: String, callback: (Int) -> Unit) {
        withContext(Dispatchers.IO) {
            callback(dao.getCount(category))
        }
    }

    suspend fun getList(callback: (List<CategoryEntity>) -> Unit) {
        withContext(Dispatchers.IO) {
            callback(dao.select())
        }
    }

    suspend fun getCategory(id: Int, callback: (String) -> Unit) {
        withContext(Dispatchers.IO) {
            callback(dao.selectById(id))
        }
    }

    suspend fun update(entity: CategoryEntity) : Int {
        var result = 0
        withContext(Dispatchers.IO) {
            result = dao.update(entity)
        }
        return result
    }

    suspend fun delete(entity: CategoryEntity) {
        withContext(Dispatchers.IO) {
            dao.delete(entity)
        }
    }

    companion object {
        @Volatile
        private var instance: CategoryRepository? = null

        fun getInstance(categoryDao: CategoryDao) =
            instance ?: synchronized(this) {
                instance ?: CategoryRepository(categoryDao).also { instance = it }
            }
    }
}