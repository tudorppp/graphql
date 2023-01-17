package com.nespresso.myapplication.entity

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonNames


@kotlinx.serialization.Serializable
data class CountryContainer(
    val country: Country,
) {
    @OptIn(ExperimentalSerializationApi::class)
    @kotlinx.serialization.Serializable
    data class Country(
        @JsonNames("available_regions") val availableRegions: List<Region>? = listOf()
    )
}

@kotlinx.serialization.Serializable
data class Region(
    val code: String,
    val name: String,
    val id: Int
)
