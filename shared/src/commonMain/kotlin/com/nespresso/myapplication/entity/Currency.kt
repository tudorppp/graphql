package com.nespresso.myapplication.entity

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonNames

@kotlinx.serialization.Serializable
data class CurrencyContainer(
    val currency: Currency
)

@OptIn(ExperimentalSerializationApi::class)
@kotlinx.serialization.Serializable
data class Currency(
    @JsonNames("base_currency_symbol") val baseCurrencySymbol: String? = null,
    @JsonNames("base_currency_code") val baseCurrencyCode: String? = null
)
