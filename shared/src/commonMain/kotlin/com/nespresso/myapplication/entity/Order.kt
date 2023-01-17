package com.nespresso.myapplication.entity

@kotlinx.serialization.Serializable
data class Order(
    val billingAddress: Address,
    val id: String,
    val items: List<Item>,
    val number: String,
    val orderDate: String,
    val paymentMethods: List<PaymentMethod>,
    val shippingAddress: Address,
    val shippingMethod: String? = null,
    val status: String,
    val total: Total
) {
    @kotlinx.serialization.Serializable
    data class Item(
        val id: String,
        val image: Image,
        val productName: String,
        val productSalePrice: Price,
        val productSku: String,
        val quantityOrdered: Int,
        val selectedOptions: List<Option>
    ) {
        @kotlinx.serialization.Serializable
        data class Option(
            val id: String,
            val value: String
        )
    }

    @kotlinx.serialization.Serializable
    data class PaymentMethod(
        val name: String,
        val type: String,
    )

    @kotlinx.serialization.Serializable
    data class Total(
        val discounts: List<PricesItem> = listOf(),
        val totalShipping: Price,
        val totalTax: Price,
        val subtotal: Price,
        val grandTotal: Price
    )
}

@kotlinx.serialization.Serializable
data class CheckoutOrder(
    val order: Order
) {
    @kotlinx.serialization.Serializable
    data class Order(
        val orderNumber: String,
        val redirectionUrl: String? = null
    )
}