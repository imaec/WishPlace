package com.imaec.wishplace.room.dao

import androidx.room.*
import com.imaec.wishplace.room.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Insert
    fun insert(entity: CategoryEntity)

    @Query("SELECT COUNT(*) FROM CategoryENTITY WHERE category = :category")
    fun getCount(category: String) : Int

    @Query("SELECT * FROM CategoryENTITY")
    fun select() : List<CategoryEntity>

    @Query("SELECT category FROM CategoryENTITY WHERE categoryId = :id")
    fun selectById(id: Int) : String

    @Update
    fun update(entity: CategoryEntity) : Int

    @Delete
    fun delete(entity: CategoryEntity)
}