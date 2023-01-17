package com.nespresso.myapplication.entity

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonNames

@kotlinx.serialization.Serializable
data class Product(
    val __typename: String,
    val id: Int,
    val sku: String,
    val name: String,
    val priceRange: PriceRange,
    val categories: List<Category> = listOf(),
    val description: Description,
    val shortDescription: Description? = null,
    val thumbnail: Image,
    val mediaGallery: List<Image>,
    val intesity: Int? = null,
    val aromaticProfile: Int? = null,
    val aromaticFilter: String? = null,
    val vertoCupSize: String? = null,
    val cupSizeDup: String? = null,
    val body: String? = null,
    val bitterness: String? = null,
    val acidity: String? = null,
    val roasting: String? = null,
    val roastingLabel: String? = null,
    val origin: String? = null,
    val coffeeAromaticProfile: String? = null,
    val upsellProducts: List<Product> = listOf(),
    val weight: Double? = null,
    val variants: List<VariantItem> = listOf(),
    val items: List<OptionItem> = listOf(),
    val isMilk: Int? = null,
    val machineCoffeeCups: String? = null,
    val machineDimensions: String? = null,
    val configurableOptions: List<ConfigurableOption> = listOf(),
    val coffeeVolumeControl: String? = null,
    val macCapsuleContainerCapacity: String? = null,
    val machinePreheatingTime: String? = null,
    val machineWaterTank: String? = null,
    val pressurePump: String? = null,
    val stockStatus: String? = null,
    val newFromDate: String? = null,
) {
    @kotlinx.serialization.Serializable
    data class Category(
        val id: Int
    )

    @kotlinx.serialization.Serializable
    data class Description(
        val html: String
    )

    @kotlinx.serialization.Serializable
    data class PriceRange(
        val minimumPrice: MinimumPrice
    ) {
        @kotlinx.serialization.Serializable
        data class MinimumPrice(
            val discount: Discount,
            val finalPrice: Price
        ) {
            @kotlinx.serialization.Serializable
            data class Discount(
                val amountOff: Double
            )
        }
    }

    @kotlinx.serialization.Serializable
    data class VariantItem(
        val product: Product,
        val attributes: List<Attribute>
    ) {
        @kotlinx.serialization.Serializable
        data class Product(
            val sku: String,
            val name: String,
            val mediaGallery: List<Image>,
            val priceRange: PriceRange,
            val stockStatus: String? = null
        )

        @kotlinx.serialization.Serializable
        data class Attribute(
            val code: String,
            val valueIndex: Int
        )
    }

    @kotlinx.serialization.Serializable
    data class OptionItem(
        val optionId: Int,
        val type: Type? = null,
        val options: List<Option>
    ) {

        @OptIn(ExperimentalSerializationApi::class)
        @kotlinx.serialization.Serializable
        enum class Type {
            @JsonNames("radio")
            Radio,

            @JsonNames("multi")
            Multi,

            @JsonNames("other")
            Other,
        }

        @kotlinx.serialization.Serializable
        data class Option(
            val id: Int,
            val quantity: Int,
            val position: Int,
            val product: Product? = null
        ) {
            @kotlinx.serialization.Serializable
            data class Product(
                val name: String,
                val thmbnail: Image
            )
        }
    }

    @kotlinx.serialization.Serializable
    data class ConfigurableOption(
        val values: List<Value>
    ) {
        @kotlinx.serialization.Serializable
        data class Value(
            val valueIndex: Int? = null,
            val swatchData: SwatchData? = null
        ) {
            @kotlinx.serialization.Serializable
            data class SwatchData(
                val value: String
            )
        }
    }
}
