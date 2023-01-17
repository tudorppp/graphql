package com.nespresso.myapplication.entity

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonNames

@kotlinx.serialization.Serializable
data class CreateEmptyCart(
    val createEmptyCart: String? = null
)

@kotlinx.serialization.Serializable
data class CartContainer(
    val cart: Cart? = null,
    val promoPopup: Cart.PromoPopup? = null
) {
    init {
        cart?.promoPopup = promoPopup
    }
}

@kotlinx.serialization.Serializable
data class MergeCarts(
    val mergeCarts: Cart? = null
)

@kotlinx.serialization.Serializable
data class RemoveItemFromCart(
    val removeItemFromCart: CartContainer? = null
)

@kotlinx.serialization.Serializable
data class UpdateCartItems(
    val updateCartItems: CartContainer? = null
)

@kotlinx.serialization.Serializable
data class AddSimpleProductsToCart(
    val addSimpleProductsToCart: CartContainer? = null
)

@kotlinx.serialization.Serializable
data class AddBundleProductsToCart(
    val addBundleProductsToCart: CartContainer? = null
)

@kotlinx.serialization.Serializable
data class AddConfigurableProductsToCart(
    val addConfigurableProductsToCart: CartContainer? = null
)

@kotlinx.serialization.Serializable
data class ApplyCouponToCart(
    val applyCouponToCart: CartContainer? = null
)

@kotlinx.serialization.Serializable
data class RemoveCouponFromCart(
    val removeCouponFromCart: CartContainer? = null
)

@kotlinx.serialization.Serializable
data class ReorderItems(
    val reorderItems: CartContainer? = null
)

@kotlinx.serialization.Serializable
data class SetShippingMethodsOnCart(
    val setShippingMethodsOnCart: CartContainer? = null,
    val setRecycleOnCart: CartContainer? = null
) {
    fun cart() =
        if (setRecycleOnCart != null) {
            setRecycleOnCart.cart
        } else {
            setShippingMethodsOnCart?.cart
        }
}

@kotlinx.serialization.Serializable
data class SetShippingAddressesOnCart(
    val setShippingAddressesOnCart: CartContainer? = null
)

@kotlinx.serialization.Serializable
data class SetBillingAddressOnCart(
    val setBillingAddressOnCart: CartContainer? = null
)

@kotlinx.serialization.Serializable
data class SetShopOnCart(
    val setShopOnCart: CartContainer? = null
)

@kotlinx.serialization.Serializable
data class SetPaymentMethodAndPlaceOrder(
    val placeOrder: CheckoutOrder? = null
)

@kotlinx.serialization.Serializable
data class CheckoutUrl(
    val requestCheckoutUrl: RequestCheckoutUrl? = null
) {
    @kotlinx.serialization.Serializable
    data class RequestCheckoutUrl(
        val redirectUrl: String? = null
    )
}

@kotlinx.serialization.Serializable
data class EmailAvailability(
    val isEmailAvailable: Boolean
)

@OptIn(ExperimentalSerializationApi::class)
@kotlinx.serialization.Serializable
data class Cart(
    val id: String,
    val items: List<Item>,
    val prices: Prices,
    @JsonNames("available_payment_methods") val availablePaymentMethods: List<PaymentMethod>,
    @JsonNames("shipping_addresses") val shippingAddresses: List<ShippingAddress>,
    @JsonNames("billing_address") val billingAddress: Address? = null,
    @JsonNames("applied_coupons") val appliedCoupons: List<Coupon>? = listOf(),
    @JsonNames("warning_messages") val warningMessages: List<String>? = listOf(),
) {
    var promoPopup: PromoPopup? = null

    @kotlinx.serialization.Serializable
    data class Item(
        val id: String,
        val product: OrderProduct,
        val prices: Prices,
        val quantity: Int,
        @JsonNames("configurable_options") val configurableOptions: List<ConfigurableOption>? = listOf(),
        @JsonNames("bundle_options") val bundleOptions: List<BundleOption>? = listOf()
    ) {
        @kotlinx.serialization.Serializable
        data class Prices(
            val price: Price,
            @JsonNames("row_total_including_tax") val rowTotalIncludingTax: Price
        )

        @kotlinx.serialization.Serializable
        data class ConfigurableOption(
            @JsonNames("option_label") val optionLabel: String,
            @JsonNames("value_label") val valueLabel: String,
            @JsonNames("value_id") val valueId: Int
        )

        @kotlinx.serialization.Serializable
        data class BundleOption(
            val label: String,
            val type: String,
            val values: List<Value>
        ) {
            @kotlinx.serialization.Serializable
            data class Value(
                val label: String
            )
        }
    }

    @kotlinx.serialization.Serializable
    data class Prices(
        val discounts: List<PricesItem>? = listOf(),
        @JsonNames("applied_taxes") val appliedTaxes: List<PricesItem>,
        @JsonNames("subtotal_including_tax") val subtotalIncludingTax: Price,
        @JsonNames("subtotal_excluding_tax") val subtotalExcludingTax: Price,
        @JsonNames("grand_total") val grandTotal: Price
    )

    @kotlinx.serialization.Serializable
    data class PaymentMethod(
        val code: String,
        val title: String
    )

    @kotlinx.serialization.Serializable
    data class Coupon(
        val code: String,
    )

    @kotlinx.serialization.Serializable
    data class PromoPopup(
        val blocks: List<BlockContainer>? = listOf()
    ) {

        @kotlinx.serialization.Serializable
        data class BlockContainer(
            val block: CmsBlock
        )
    }
}