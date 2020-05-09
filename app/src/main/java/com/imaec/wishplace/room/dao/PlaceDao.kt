package com.imaec.wishplace.room.dao

import androidx.room.Dao
import androidx.room.Insert
import com.imaec.wishplace.room.entity.PlaceEntity

@Dao
interface PlaceDao {

    @Insert
    fun insert(entity: PlaceEntity)
}