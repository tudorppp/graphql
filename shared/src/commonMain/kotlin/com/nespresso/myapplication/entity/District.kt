package com.nespresso.myapplication.entity

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonNames

@kotlinx.serialization.Serializable
data class DistrictContainer(
    val districtFinder: Item
) {
    @kotlinx.serialization.Serializable
    data class Item(
        val items: List<District>
    )
}

@OptIn(ExperimentalSerializationApi::class)
@kotlinx.serialization.Serializable
data class District(
    @JsonNames("district_en") val districtEn: String,
    @JsonNames("city_en") val cityEn: String,
    @JsonNames("state_en") val stateEn: String,
    @JsonNames("district_ar") val districtAr: String,
    @JsonNames("city_ar") val cityAr: String,
    @JsonNames("state_ar") val stateAr: String,
    @JsonNames("state_id") val stateId: Int
)
