package com.imaec.wishplace.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.imaec.wishplace.room.entity.KeywordEntity

@Dao
interface KeywordDao {

    @Insert
    fun insert(entity: KeywordEntity)

    @Query("SELECT * FROM keywordENTITY")
    fun select() : List<KeywordEntity>

    @Delete
    fun delete(vararg entity: KeywordEntity)
}