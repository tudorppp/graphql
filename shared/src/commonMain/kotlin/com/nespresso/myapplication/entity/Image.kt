package com.nespresso.myapplication.entity

@kotlinx.serialization.Serializable
data class Image(
    val url: String,
    val disabled: Boolean? = null,
    val resized: List<Resized> = listOf()
) {

    @kotlinx.serialization.Serializable
    data class Resized(
        val url: String
    )
}
