package com.nespresso.myapplication.entity

@kotlinx.serialization.Serializable
data class CmsBlocksContainer(
    val cmsBlocks: Item
) {
    @kotlinx.serialization.Serializable
    data class Item(
        val items: List<CmsBlock?> = listOf()
    )
}

@kotlinx.serialization.Serializable
data class CmsBlock(
    val identifier: String,
    val title: String,
    val content: String
)
