package com.imaec.wishplace.room.dao

import androidx.room.*
import com.imaec.wishplace.model.PlaceDTO
import com.imaec.wishplace.room.entity.PlaceEntity

@Dao
interface PlaceDao {

    @Insert
    fun insert(entity: PlaceEntity)

    @Query("SELECT COUNT(*) FROM placeENTITY")
    fun selectCount() : Int

//    @Query("SELECT * FROM placeENTITY AS a WHERE saveTime IN (SELECT b.saveTime FROM placeENTITY AS b WHERE a.foreignId = b.foreignId ORDER BY b.saveTime DESC LIMIT 4)")
//    fun select() : List<PlaceEntity>

    @Query("SELECT *, category FROM placeENTITY AS a INNER JOIN categoryENTITY ON a.foreignId = categoryENTITY.categoryId WHERE saveTime IN (SELECT b.saveTime FROM placeENTITY AS b WHERE a.foreignId = b.foreignId ORDER BY b.saveTime DESC LIMIT 4)")
    fun select() : List<PlaceDTO>

    @Query("SELECT *, category FROM placeENTITY INNER JOIN categoryENTITY ON placeENTITY.foreignId = categoryENTITY.categoryId WHERE placeId=:placeId")
    fun select(placeId: Int) : PlaceDTO

    @Query("SELECT *, category FROM placeENTITY INNER JOIN categoryENTITY ON placeENTITY.foreignId = categoryENTITY.categoryId WHERE foreignId=:categoryId")
    fun selectByCategory(categoryId: Int) : List<PlaceDTO>

    @Query("SELECT *, category FROM placeENTITY INNER JOIN categoryENTITY ON placeENTITY.foreignId = categoryENTITY.categoryId WHERE name LIKE :name")
    fun selectByName(name: String) : List<PlaceDTO>

    @Query("SELECT *, category FROM placeENTITY INNER JOIN categoryENTITY ON placeENTITY.foreignId = categoryENTITY.categoryId WHERE address LIKE :address")
    fun selectByAddress(address: String) : List<PlaceDTO>

    @Update
    fun update(vararg entity: PlaceEntity) : Int

    @Delete
    fun delete(entity: PlaceEntity)
}