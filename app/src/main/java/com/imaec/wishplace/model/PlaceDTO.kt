package com.imaec.wishplace.model

data class PlaceDTO(
    var title: String = "",
    var address: String = "",
    var site: String = "",
    var imgUrl: String = "",
    var isVisit: Boolean = false
)