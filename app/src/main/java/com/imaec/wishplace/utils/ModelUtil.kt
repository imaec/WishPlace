package com.imaec.wishplace.utils

import com.imaec.wishplace.model.PlaceDTO
import com.imaec.wishplace.room.entity.PlaceEntity

class ModelUtil {

    companion object {

        fun toPlaceEntity(dto: PlaceDTO?) : PlaceEntity {
            dto?.let {
                return PlaceEntity(
                    placeId = it.placeId,
                    foreignId = it.foreignId,
                    name = it.name,
                    address = it.address,
                    siteUrl = it.siteUrl,
                    imageUrl = it.imageUrl,
                    content = it.content,
                    saveTime = it.saveTime,
                    visitFlag = it.visitFlag
                )
            } ?: run {
                return PlaceEntity()
            }
        }
    }
}