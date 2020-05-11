package com.imaec.wishplace.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.imaec.wishplace.room.entity.PlaceEntity

@Dao
interface PlaceDao {

    @Insert
    fun insert(entity: PlaceEntity)

    @Query("SELECT COUNT(*) FROM placeENTITY")
    fun selectCount() : Int

    @Query("SELECT * FROM placeENTITY AS a WHERE name IN (SELECT b.name FROM placeENTITY AS b WHERE a.foreignId = b.foreignId ORDER BY b.name ASC LIMIT 4)")
    fun select() : List<PlaceEntity>
}