package com.nespresso.myapplication.entity

@kotlinx.serialization.Serializable
data class OrderProduct(
    val __typename: String,
    val sku: String,
    val name: String,
    val thumbnail: Image,
    val categories: List<Category> = listOf()
) {

    @kotlinx.serialization.Serializable
    data class Category(
        val id: Int
    )
}