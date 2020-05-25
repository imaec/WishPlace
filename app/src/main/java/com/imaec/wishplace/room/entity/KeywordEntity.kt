package com.imaec.wishplace.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.imaec.wishplace.utils.Utils

@Entity
data class KeywordEntity(
    @PrimaryKey(autoGenerate = true) var keywordId: Int = 0,
    @ColumnInfo var keyword: String = "",
    @ColumnInfo var saveTime: String = Utils.getDate("yyyyMMddHHmmss")
)