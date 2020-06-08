package com.imaec.wishplace.room.dao

import androidx.room.*
import com.imaec.wishplace.room.entity.PlaceEntity

@Dao
interface PlaceDao {

    @Insert
    fun insert(entity: PlaceEntity)

    @Query("SELECT COUNT(*) FROM placeENTITY")
    fun selectCount() : Int

    @Query("SELECT * FROM placeENTITY AS a WHERE saveTime IN (SELECT b.saveTime FROM placeENTITY AS b WHERE a.foreignId = b.foreignId ORDER BY b.saveTime DESC LIMIT 4)")
    fun select() : List<PlaceEntity>

    @Query("SELECT * FROM placeENTITY WHERE placeId=:placeId")
    fun select(placeId: Int) : PlaceEntity

    @Query("SELECT * FROM placeENTITY WHERE foreignId=:categoryId")
    fun selectByCategory(categoryId: Int) : List<PlaceEntity>

    @Query("SELECT * FROM placeENTITY WHERE name LIKE :name")
    fun selectByName(name: String) : List<PlaceEntity>

    @Query("SELECT * FROM placeENTITY WHERE address LIKE :address")
    fun selectByAddress(address: String) : List<PlaceEntity>

    @Update
    fun update(vararg entity: PlaceEntity) : Int

    @Delete
    fun delete(entity: PlaceEntity)
}