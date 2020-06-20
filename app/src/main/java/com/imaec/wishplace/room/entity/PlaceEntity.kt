package com.imaec.wishplace.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.imaec.wishplace.utils.Utils

@Entity(tableName = "placeENTITY")
data class PlaceEntity(
    @PrimaryKey(autoGenerate = true) var placeId: Int = 0,
    @ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["categoryId"],
        childColumns = ["foreignId"]
    ) var foreignId: Int = 0,
    @ColumnInfo var name: String = "",
    @ColumnInfo var address: String = "",
    @ColumnInfo var siteUrl: String = "",
    @ColumnInfo var content: String = "",
    @ColumnInfo var imageUrl: String = "",
    @ColumnInfo var saveTime: String = Utils.getDate("yyyyMMddHHmmss"),
    @ColumnInfo var visitFlag: Boolean = false
)