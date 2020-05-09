package com.imaec.wishplace.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "PlaceENTITY")
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
    @ColumnInfo var imageUrl: String = "",
    @ColumnInfo var visitFlag: Boolean = false
)