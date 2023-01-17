package com.nespresso.myapplication.entity

@kotlinx.serialization.Serializable
data class Price(
    val value: Double,
    val currency: String,
)

@kotlinx.serialization.Serializable
data class PricesItem(
    val label: String,
    val amount: Price
)
