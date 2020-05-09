package com.imaec.wishplace.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.imaec.wishplace.room.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Query("SELECT COUNT(*) FROM CATEGORYENTITY where category = :category")
    fun getCount(category: String) : Int

    @Insert
    fun insert(entity: CategoryEntity)
}