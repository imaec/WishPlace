package com.imaec.wishplace.room.dao

import androidx.room.*
import com.imaec.wishplace.room.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Insert
    fun insert(entity: CategoryEntity)

    @Query("SELECT COUNT(*) FROM CategoryENTITY")
    fun getCount() : Int

    @Query("SELECT COUNT(*) FROM CategoryENTITY WHERE category = :category")
    fun getCount(category: String) : Int

    @Query("SELECT * FROM CategoryENTITY")
    fun select() : List<CategoryEntity>

    @Update
    fun update(entity: CategoryEntity) : Int

    @Delete
    fun delete(entity: CategoryEntity)
}