package com.imaec.wishplace.model

import com.imaec.wishplace.utils.Utils

data class PlaceDTO(
    var placeId: Int = 0,
    var foreignId: Int = 0,
    var category: String = "",
    var name: String = "",
    var address: String = "",
    var siteUrl: String = "",
    var content: String = "",
    var imageUrl: String = "",
    var saveTime: String = Utils.getDate("yyyyMMddHHmmss"),
    var visitFlag: Boolean = false
)