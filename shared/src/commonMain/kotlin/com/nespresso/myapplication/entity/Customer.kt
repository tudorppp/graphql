package com.nespresso.myapplication.entity

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonNames

@kotlinx.serialization.Serializable
data class CustomerContainer(
    val customer: Customer? = null
)

@kotlinx.serialization.Serializable
data class CreateCustomerContainer(
    val createCustomer: CustomerContainer? = null
)

@kotlinx.serialization.Serializable
data class ChangeCustomerPassword(
    val changeCustomerPassword: Email? = null
) {
    @kotlinx.serialization.Serializable
    data class Email(
        val email: String
    )
}

@kotlinx.serialization.Serializable
data class UpdateCustomerContainer(
    val updateCustomer: CustomerContainer? = null
)

@kotlinx.serialization.Serializable
data class Customer(
    val id: Int? = null,
    val addresses: List<Address>,
    val email: String,
    val firstName: String,
    val lastName: String,
    //TODO this might not work. need to test it. serialization with enums...
    val customerAccountType: AccountType? = null,
    val subscribeEmail: Boolean? = null,
    val subscribePhone: Boolean? = null,
    val subscribeText: Boolean? = null,
    val password: String? = null,
    val tradePhone: String? = null,
    val customerTelephonePrefix: String? = null,
    val customercompany: String? = null,
    val taxvat: String? = null,
    val orders: Orders? = null,
    val wishlists: List<Wishlist> = listOf(),
) {

    @OptIn(ExperimentalSerializationApi::class)
    @kotlinx.serialization.Serializable
    enum class AccountType {
        @JsonNames("1")
        Business,

        @JsonNames("2")
        Personal
    }

    @kotlinx.serialization.Serializable
    data class Orders(
        val items: List<Order> = listOf()
    )
}
