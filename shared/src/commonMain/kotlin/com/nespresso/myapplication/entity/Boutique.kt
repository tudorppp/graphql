package com.nespresso.myapplication.entity

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonNames

@kotlinx.serialization.Serializable
data class BoutiqueContainer(
    val shopFinder: Item
) {

    @kotlinx.serialization.Serializable
    data class Item(
        val items: List<Boutique>
    )
}

@OptIn(ExperimentalSerializationApi::class)
@kotlinx.serialization.Serializable
data class Boutique constructor(
    @JsonNames("can_collect") val canCollect: Boolean,
    val identifier: String,
    @JsonNames("country_id") val countryId: String,
    val city: String,
    val latitude: String,
    val longitude: String,
    @JsonNames("other_opentimes_info") val otherOpentimesInfo: String,
    val postcode: String? = null,
    val region: String? = null,
    @JsonNames("shop_email") val shopEmail: String? = null,
    @JsonNames("shop_name") val shopName: String,
    val street: String,
    val telephone: String,
)
