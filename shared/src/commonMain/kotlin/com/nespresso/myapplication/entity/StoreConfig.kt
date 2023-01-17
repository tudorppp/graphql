package com.nespresso.myapplication.entity

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonNames

@kotlinx.serialization.Serializable
data class StoreConfigContainer(
    val storeConfig: StoreConfig
)

@kotlinx.serialization.Serializable
@OptIn(ExperimentalSerializationApi::class)
data class StoreConfig(
    //TODO should we parse the cities here and send a list of Strings ?
    @JsonNames("checkout_cities") val checkoutCities: String? = null,
    @JsonNames("guest_checkout") val guestCheckout: Boolean,
    @JsonNames("minimum_order_active") val minimumOrderActive: Boolean,
    @JsonNames("minimum_order_amount") val minimumOrderAmount: String? = null,
    @JsonNames("minimum_order_text") val minimumOrderText: String? = null
)
