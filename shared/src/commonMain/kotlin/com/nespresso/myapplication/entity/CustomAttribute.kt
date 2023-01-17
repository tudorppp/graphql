package com.nespresso.myapplication.entity

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonNames

@kotlinx.serialization.Serializable
data class CustomAttribute(
    val attributeCode: String,
    val value: String
)

@kotlinx.serialization.Serializable
data class CustomAttributeMetadataContainer(
    val customAttributeMetadata: Item? = null
) {
    @kotlinx.serialization.Serializable
    data class Item(
        val items: List<CustomAttributeMetadata>
    )
}

@OptIn(ExperimentalSerializationApi::class)
@kotlinx.serialization.Serializable
data class CustomAttributeMetadata(
    @JsonNames("attribute_code") val attributeCode: String? = null,
    @JsonNames("attribute_type") val attributeType: String? = null,
    @JsonNames("attribute_options") val attributeOptions: List<AttributeOptions>
) {
    @kotlinx.serialization.Serializable
    data class AttributeOptions(
        val label: String,
        val value: String,
        @JsonNames("swatch_data") val swatchData: SwatchData? = null
    ) {
        @kotlinx.serialization.Serializable
        data class SwatchData(
            val value: String
        )
    }
}

