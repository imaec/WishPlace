package com.imaec.wishplace.model

data class NaverPlaceDTO(
    var lastBuildDate: String,
    var total: Int,
    var start: Int,
    var display: Int,
    var items: ArrayList<Item>
) {
    data class Item (
        var title: String,
        var link: String,
        var category: String,
        var description: String,
        var telephone: String,
        var address: String,
        var roadAddress: String,
        var mapx: String,
        var mapy: String
    )
}