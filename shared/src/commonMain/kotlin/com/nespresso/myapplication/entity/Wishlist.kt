package com.nespresso.myapplication.entity

@kotlinx.serialization.Serializable
data class AddProductsToWishlist(
    val addProductsToWishlist: WishlistItem
)

@kotlinx.serialization.Serializable
data class RemoveProductsFromWishlist(
    val removeProductsFromWishlist: WishlistItem
)

@kotlinx.serialization.Serializable
data class WishlistItem(
    val wishlist: Wishlist
)

@kotlinx.serialization.Serializable
data class Wishlist(
    val itemsV2: List<Item>? = listOf()
) {
    @kotlinx.serialization.Serializable
    data class Item(
        val id: String,
        val product: Product
    )
}