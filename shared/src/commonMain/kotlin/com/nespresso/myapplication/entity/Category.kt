package com.nespresso.myapplication.entity


@kotlinx.serialization.Serializable
data class CategoryContainer(
    val categoryList: List<Category>
)

@kotlinx.serialization.Serializable
data class Category(
    val id: Int,
    val name: String,
    val position: Int,
    val children: List<Category> = listOf()
)
