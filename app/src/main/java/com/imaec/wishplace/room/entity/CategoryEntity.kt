package com.imaec.wishplace.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) var categoryId: Int = 0,
    @ColumnInfo var category: String = ""
)