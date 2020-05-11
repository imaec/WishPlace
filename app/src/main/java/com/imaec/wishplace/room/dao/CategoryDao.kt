package com.imaec.wishplace.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.imaec.wishplace.room.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Query("SELECT COUNT(*) FROM CategoryENTITY WHERE category = :category")
    fun getCount(category: String) : Int

    @Query("SELECT * FROM CategoryENTITY")
    fun select() : List<CategoryEntity>

    @Query("SELECT category FROM CategoryENTITY WHERE categoryId = :id")
    fun selectById(id: Int) : String

    @Insert
    fun insert(entity: CategoryEntity)
}