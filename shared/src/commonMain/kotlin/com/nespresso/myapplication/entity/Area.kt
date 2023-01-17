package com.nespresso.myapplication.entity

@kotlinx.serialization.Serializable
data class AreaContainer(
    val areaFinder: Item
) {
    @kotlinx.serialization.Serializable
    data class Item(
        val items: List<Area>
    )
}

@kotlinx.serialization.Serializable
data class Area(
    val area: String,
    val city: String
)
