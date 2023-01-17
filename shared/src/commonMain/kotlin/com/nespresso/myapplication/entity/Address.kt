package com.nespresso.myapplication.entity

@kotlinx.serialization.Serializable
data class CreateCustomerAddress(
    val createCustomerAddress: Address? = null
)

@kotlinx.serialization.Serializable
data class UpdateCustomerAddress(
    val updateCustomerAddress: Address? = null
)

@kotlinx.serialization.Serializable
data class DeleteCustomerAddress(
    val deleteCustomerAddress: Boolean? = null
)

@kotlinx.serialization.Serializable
data class Address(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val city: String,
    val telephone: String,
    val street: List<String>,
    val countryCode: String? = null,
    val regionCode: String? = null,
    val regionName: String? = null,
    val regionId: Int? = null,
    val country: AddressCountry? = null,
    val region: AddressRegion? = regionCode?.let { AddressRegion(null, regionName, it, regionId) },
    val company: String? = null,
    val postCode: String? = null,
    val defaultBilling: Boolean,
    val defaultShipping: Boolean,
    val area: String? = null,
    val house: String? = null,
    val streetName: String? = null,
    val telephonePrefix: String? = null,
    val landmark: String? = null,
    val district: String? = null,
    val addressSuburb: String? = null,
    val vatNumber: String? = null
)

@kotlinx.serialization.Serializable
data class ShippingAddress(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val city: String,
    val telephone: String,
    val street: List<String>,
    val countryCode: String? = null,
    val regionCode: String? = null,
    val regionName: String? = null,
    val regionId: Int? = null,
    val country: AddressCountry? = null,
    val region: AddressRegion? = regionCode?.let {
        AddressRegion(
            null,
            regionName,
            it,
            regionId
        )
    },
    val company: String? = null,
    val postCode: String? = null,
    val defaultBilling: Boolean,
    val defaultShipping: Boolean,
    val area: String? = null,
    val house: String? = null,
    val streetName: String? = null,
    val telephonePrefix: String? = null,
    val landmark: String? = null,
    val district: String? = null,
    val addressSuburb: String? = null,
    val vatNumber: String? = null,
    val availableShippingMethods: List<ShippingMethod> = listOf(),
    val selectedShippingMethod: SelectedShippingMethod? = null
) {
    @kotlinx.serialization.Serializable
    data class ShippingMethod(
        val carrierCode: String,
        val carrierTitle: String,
        val methodCode: String,
        val methodTitle: String? = null,
        val priceInclTax: Price,
        val amount: Price
    )

    @kotlinx.serialization.Serializable
    data class SelectedShippingMethod(
        val amount: Price
    )
}


@kotlinx.serialization.Serializable
data class AddressRegion(
    val code: String?,
    val region: String?,
    val regionCode: String?,
    val regionId: Int?,
)

@kotlinx.serialization.Serializable
data class AddressCountry(
    val code: String
)