package com.nespresso.myapplication.module

import com.nespresso.myapplication.core.*
import com.nespresso.myapplication.entity.*
import com.nespresso.myapplication.helper.execute

class CartModule {
    sealed class CartType {
        abstract fun id(): String
        data class Customer(val id: String) : CartType() {
            override fun id(): String {
                return id
            }
        }

        data class Guest(val id: String) : CartType() {
            override fun id(): String {
                return id
            }
        }
    }

    sealed class ProductType {
        data class Simple(
            val sku: String,
            val qty: Int
        ) : ProductType()

        data class Configurable(
            val parentSku: String,
            val sku: String,
            val qty: Int
        ) :
            ProductType()

        data class Bundle(
            val sku: String,
            val qty: Int,
            val options: List<Triple<Int, Int, List<String>>>
        ) : ProductType()
    }

    companion object {

        private fun QueryField.addressField(
            name: String,
            country: Country = Config.country,
            children: (Field) -> List<Field> = { listOf() },
        ): Field {
            return field(name) {
                field("firstname")
                field("lastname")
                field("city")
                field("postcode")
                field("street")
                field("telephone")
                field("company")
                field("country") {
                    field("code")
                }
                field("region") {
                    field("code")
                }
                when (country) {
                    Country.Ae -> {
                        field("area")
                        field("house")
                        field("street_name")
                        field("telephone_prefix")
                        field("landmark")
                    }
                    Country.Sa -> {
                        field("district")
                    }
                    Country.Za -> {
                        field("address_suburb")
                        field("vat_number")
                    }
                    else -> {}
                }
                val iterator = children(this).iterator()
                while (iterator.hasNext()) {
                    iterator.next()
                }
            }
        }

        private fun Operation.cartField(name: String, country: Country = Config.country): Field =
            field(name) {
                field("id")
                field("items") {
                    field("id")
                    field("quantity")
                    if (country == Country.Ae) {
                        field("product") {
                            field("__typename")
                            field("name")
                            field("sku")
                            field("categories") {
                                field("id")
                            }
                            field("thumbnail") {
                                field("url")
                                field("disabled")
                                field(
                                    "resized",
                                    dictionaryOf("image_ids" to Arg.String("category_page_grid"))
                                ) {
                                    field("url")
                                }
                            }
                        }
                    } else {
                        field("product") {
                            field("__typename")
                            field("name")
                            field("sku")
                            field("categories") {
                                field("id")
                            }
                        }
                    }
                    field("prices") {
                        field("price") {
                            field("value")
                            field("currency")
                        }
                        field("row_total_including_tax") {
                            field("value")
                            field("currency")
                        }
                    }
                    inlineFragmentPartially("ConfigurableCartItem") {
                        field("configurable_options") {
                            field("option_label")
                            field("value_label")
                            field("value_id")
                        }
                    }
                    inlineFragmentPartially("BundleCartItem") {
                        field("bundle_options") {
                            field("type")
                            field("label")
                            field("values") {
                                field("label")
                            }
                        }
                    }
                }
                field("prices") {
                    field("discounts") {
                        field("label")
                        field("amount") {
                            field("value")
                            field("currency")
                        }
                    }
                    field("applied_taxes") {
                        field("label")
                        field("amount") {
                            field("value")
                            field("currency")
                        }
                    }
                    field("subtotal_including_tax") {
                        field("value")
                        field("currency")
                    }
                    field("subtotal_excluding_tax") {
                        field("value")
                        field("currency")
                    }
                    field("grand_total") {
                        field("value")
                        field("currency")
                    }
                }
                field("available_payment_methods") {
                    field("code")
                    field("title")
                }
                addressField(
                    name = "shipping_addresses",
                    children = {
                        listOf(
                            it.field("available_shipping_methods") {
                                field("carrier_code")
                                field("carrier_title")
                                field("method_code")
                                field("method_title")
                                field("price_incl_tax") {
                                    field("value")
                                    field("currency")
                                }
                                field("amount") {
                                    field("value")
                                    field("currency")
                                }
                            },
                            it.field("selected_shipping_method") {
                                field("amount") {
                                    field("value")
                                    field("currency")
                                }
                            }
                        )
                    }
                )
                addressField("billing_address")
                field("applied_coupons") {
                    field("code")
                }
                field("warning_messages")
            }

        private fun cartQuery(
            name: String,
            args: Arg.Dictionary = Arg.Dictionary(mapOf()),
            country: Country = Config.country
        ) =
            query(name, args) {
                field("id")
                field("items") {
                    field("id")
                    field("quantity")
                    if (country == Country.Ae) {
                        field("product") {
                            field("__typename")
                            field("name")
                            field("sku")
                            field("categories") {
                                field("id")
                            }
                            field("thumbnail") {
                                field("url")
                                field("disabled")
                                field(
                                    "resized",
                                    dictionaryOf("image_ids" to Arg.String("category_page_grid"))
                                ) {
                                    field("url")
                                }
                            }
                        }
                    } else {
                        field("product") {
                            field("__typename")
                            field("name")
                            field("sku")
                            field("categories") {
                                field("id")
                            }
                        }
                    }
                    field("prices") {
                        field("price") {
                            field("value")
                            field("currency")
                        }
                        field("row_total_including_tax") {
                            field("value")
                            field("currency")
                        }
                    }
                    inlineFragmentPartially("ConfigurableCartItem") {
                        field("configurable_options") {
                            field("option_label")
                            field("value_label")
                            field("value_id")
                        }
                    }
                    inlineFragmentPartially("BundleCartItem") {
                        field("bundle_options") {
                            field("type")
                            field("label")
                            field("values") {
                                field("label")
                            }
                        }
                    }
                }
                field("prices") {
                    field("discounts") {
                        field("label")
                        field("amount") {
                            field("value")
                            field("currency")
                        }
                    }
                    field("applied_taxes") {
                        field("label")
                        field("amount") {
                            field("value")
                            field("currency")
                        }
                    }
                    field("subtotal_including_tax") {
                        field("value")
                        field("currency")
                    }
                    field("subtotal_excluding_tax") {
                        field("value")
                        field("currency")
                    }
                    field("grand_total") {
                        field("value")
                        field("currency")
                    }
                }
                field("available_payment_methods") {
                    field("code")
                    field("title")
                }
                addressField(
                    name = "shipping_addresses",
                    children = {
                        listOf(
                            it.field("available_shipping_methods") {
                                field("carrier_code")
                                field("carrier_title")
                                field("method_code")
                                field("method_title")
                                field("price_incl_tax") {
                                    field("value")
                                    field("currency")
                                }
                                field("amount") {
                                    field("value")
                                    field("currency")
                                }
                            },
                            it.field("selected_shipping_method") {
                                field("amount") {
                                    field("value")
                                    field("currency")
                                }
                            }
                        )
                    }
                )
                addressField("billing_address")
                field("applied_coupons") {
                    field("code")
                }
                field("warning_messages")
            }

        fun QueryField.promoPopupField(country: Country = Config.country): Field? {
            return if (country in listOf(Country.Ae, Country.Sa)) {
                field("promo_popup") {
                    field("blocks") {
                        field("block") {
                            field("content")
                            field("identifier")
                            field("title")
                        }
                    }
                }
            } else null
        }

        suspend fun createCartMutation(token: String? = null) =
            execute<CreateEmptyCart>(
                operations = listOf(
                    mutation(
                        mutationName = "createEmptyCart"
                    )
                ),
                operationType = OperationType.Mutation,
                token = token
            ).map { it?.createEmptyCart }

        suspend fun cartQuery(cartType: CartType, token: String? = null) =
            execute<CartContainer>(
                operations = listOf(
                    cartQuery(
                        name = "cart",
                        args = dictionaryOf("cart_id" to Arg.String(cartType.id()))
                    )
                ),
                operationType = OperationType.Query,
                token = token
            ).map { it?.cart }

        //TODO cart() "customerCart" ? Still don't know if it's necessary or we can just use the cart above

        suspend fun mergeCartsQuery(
            sourceCartId: String,
            destinationCartId: String,
            token: String? = null
        ) =
            execute<MergeCarts>(
                operations = listOf(
                    cartQuery(
                        name = "mergeCarts",
                        args = dictionaryOf(
                            "source_cart_id" to Arg.String(sourceCartId),
                            "destination_cart_id" to Arg.String(destinationCartId)
                        )
                    )
                ),
                operationType = OperationType.Query,
                token = token
            ).map { it?.mergeCarts }

        suspend fun removeItemMutation(cartType: CartType, itemId: Int, token: String? = null) =
            execute<RemoveItemFromCart>(
                operations = listOf(
                    mutation(
                        mutationName = "removeItemFromCart",
                        attributes = dictionaryOf(
                            "input" to dictionaryOf(
                                "cart_id" to Arg.String(cartType.id()),
                                "cart_item_id" to Arg.Int(itemId)
                            )
                        )
                    ) {
                        cartField("cart")
                        promoPopupField()
                    }
                ),
                operationType = OperationType.Mutation,
                token = token
            ).map { it?.removeItemFromCart?.cart }

        suspend fun removeItemsMutation(cartType: CartType, itemIds: List<Int>) =
            updateItemsMutation(
                cartType = cartType,
                items = itemIds.map { it to 0 }
            )

        suspend fun updateItemsMutation(
            cartType: CartType,
            items: List<Pair<Int, Int>>,
            token: String? = null
        ) =
            execute<UpdateCartItems>(
                operations = listOf(
                    mutation(
                        mutationName = "updateCartItems",
                        attributes = dictionaryOf(
                            "input" to dictionaryOf(
                                "cart_id" to Arg.String(cartType.id()),
                                "cart_items" to Arg.Array(
                                    items.map {
                                        dictionaryOf(
                                            "cart_item_id" to Arg.Int(it.first),
                                            "quantity" to Arg.Int(it.second)
                                        )
                                    }
                                )
                            )
                        )
                    ) {
                        cartField("cart")
                        promoPopupField()
                    }
                ),
                operationType = OperationType.Mutation,
                token = token
            ).map { it?.updateCartItems?.cart }


        //TODO might want to move these in a separate class?
        suspend fun addProductsMutation(
            cartType: CartType,
            products: List<ProductType>,
            token: String? = null
        ): Result<List<SdkError>, Cart?> {
            val productType = products.first()
            when (productType) {
                is ProductType.Simple -> {
                    val mutation = mutation(
                        mutationName = "addSimpleProductsToCart",
                        attributes = dictionaryOf(
                            "input" to dictionaryOf(
                                "cart_id" to Arg.String(cartType.id()),
                                "cart_items" to Arg.Array(
                                    products
                                        .filterIsInstance<ProductType.Simple>()
                                        .map {
                                            dictionaryOf(
                                                "data" to dictionaryOf(
                                                    "sku" to Arg.String(it.sku),
                                                    "quantity" to Arg.Int(it.qty),
                                                )
                                            )
                                        }
                                )
                            )
                        )
                    ) {
                        cartField("cart")
                        promoPopupField()
                    }
                    return execute<AddSimpleProductsToCart>(
                        operations = listOf(
                            mutation
                        ),
                        operationType = OperationType.Mutation,
                        token = token
                    ).map { it?.addSimpleProductsToCart?.cart }
                }
                is ProductType.Configurable -> {
                    val mutation = mutation(
                        mutationName = "addConfigurableProductsToCart",
                        attributes = dictionaryOf(
                            "input" to dictionaryOf(
                                "cart_id" to Arg.String(cartType.id()),
                                "cart_items" to Arg.Array(
                                    products
                                        .filterIsInstance<ProductType.Configurable>()
                                        .map {
                                            dictionaryOf(
                                                "parent_sku" to Arg.String(it.parentSku),
                                                "data" to dictionaryOf(
                                                    "sku" to Arg.String(it.sku),
                                                    "quantity" to Arg.Int(it.qty),
                                                )
                                            )
                                        }
                                )
                            )
                        )
                    ) {
                        cartField("cart")
                        promoPopupField()
                    }
                    return execute<AddConfigurableProductsToCart>(
                        operations = listOf(
                            mutation
                        ),
                        operationType = OperationType.Mutation,
                        token = token
                    ).map { it?.addConfigurableProductsToCart?.cart }
                }
                is ProductType.Bundle -> {
                    val mutation = mutation(
                        mutationName = "addBundleProductsToCart",
                        attributes = dictionaryOf(
                            "input" to dictionaryOf(
                                "cart_id" to Arg.String(cartType.id()),
                                "cart_items" to Arg.Array(
                                    products
                                        .filterIsInstance<ProductType.Bundle>()
                                        .map {
                                            dictionaryOf(
                                                "data" to dictionaryOf(
                                                    "sku" to Arg.String(it.sku),
                                                    "quantity" to Arg.Int(it.qty),
                                                ),
                                                "bundle_options" to Arg.Array(
                                                    it.options.map {
                                                        dictionaryOf(
                                                            "id" to Arg.Int(it.first),
                                                            "quantity" to Arg.Int(it.second),
                                                            "value" to Arg.Array(it.third.map {
                                                                Arg.String(
                                                                    it
                                                                )
                                                            })
                                                        )
                                                    }
                                                )
                                            )
                                        }
                                )
                            )
                        )
                    ) {
                        cartField("cart")
                        promoPopupField()
                    }
                    return execute<AddBundleProductsToCart>(
                        operations = listOf(
                            mutation
                        ),
                        operationType = OperationType.Mutation,
                        token = token
                    ).map { it?.addBundleProductsToCart?.cart }
                }
            }
        }

        suspend fun addCoupon(cartType: CartType, code: String, token: String? = null) =
            execute<ApplyCouponToCart>(
                operations = listOf(
                    mutation(
                        mutationName = "applyCouponToCart",
                        attributes = dictionaryOf(
                            "input" to dictionaryOf(
                                "cart_id" to Arg.String(cartType.id()),
                                "coupon_code" to Arg.String(code)
                            )
                        )
                    ) {
                        cartField("cart")
                    }
                ),
                operationType = OperationType.Mutation,
                token = token
            ).map { it?.applyCouponToCart?.cart }

        suspend fun removeCoupon(cartType: CartType, token: String? = null) =
            execute<RemoveCouponFromCart>(
                operations = listOf(
                    mutation(
                        mutationName = "removeCouponFromCart",
                        attributes = dictionaryOf(
                            "input" to dictionaryOf(
                                "cart_id" to Arg.String(cartType.id()),
                            )
                        )
                    ) {
                        cartField("cart")
                    }
                ),
                operationType = OperationType.Mutation,
                token = token
            ).map { it?.removeCouponFromCart?.cart }

        suspend fun reorderMutation(orderNumber: String, token: String? = null) =
            execute<ReorderItems>(
                operations = listOf(
                    mutation(
                        mutationName = "reorderItems",
                        attributes = dictionaryOf(
                            "orderNumber" to Arg.String(orderNumber)
                        )
                    ) {
                        cartField("cart")
                    }
                ),
                operationType = OperationType.Mutation,
                token = token
            ).map { it?.reorderItems?.cart }

        suspend fun setShippingMethodsMutation(
            cartType: CartType,
            carrierCode: String,
            methodCode: String,
            recyclingBag: Boolean? = null,
            token: String? = null
        ) = execute<SetShippingMethodsOnCart>(
            operations = listOf(
                mutation(
                    mutationName = "setShippingMethodsOnCart",
                    attributes = dictionaryOf(
                        "input" to dictionaryOf(
                            "cart_id" to Arg.String(cartType.id()),
                            "shipping_methods" to Arg.Array(
                                listOf(
                                    dictionaryOf(
                                        "carrier_code" to Arg.String(carrierCode),
                                        "method_code" to Arg.String(methodCode)
                                    )
                                )
                            )
                        )
                    )
                ) {
                    if (recyclingBag != null) {
                        field("cart") {
                            field("id")
                        }
                    } else {
                        cartField("cart")
                    }
                },
                recyclingBag?.let {
                    mutation(
                        mutationName = "setRecycleOnCart",
                        attributes = dictionaryOf(
                            "input" to dictionaryOf(
                                "cart_id" to Arg.String(cartType.id()),
                                "recycle" to Arg.Bool(it)
                            )
                        )
                    ) {
                        cartField("cart")
                    }
                }
            ),
            operationType = OperationType.Mutation,
            token = token
        ).map { it?.cart() }

        private fun addressArgs(address: Address, country: Country = Config.country) =
            dictionaryOf(
                "firstname" to Arg.String(address.firstName),
                "lastname" to Arg.String(address.lastName),
                "company" to Arg.String(address.company ?: ""),
                "street" to Arg.Array(address.street.map { Arg.String(it) }),
                "city" to Arg.String(address.city),
                "region" to Arg.String(address.region?.regionCode ?: ""),
                "country_code" to Arg.String(address.countryCode ?: ""),
                "postcode" to Arg.String(address.postCode ?: ""),
                "telephone" to Arg.String(address.telephone),
                "save_in_address_book" to Arg.Bool(false),
                when (country) {
                    Country.Ae -> {
                        "area" to Arg.String(address.area ?: "")
                        "house" to Arg.String(address.house ?: "")
                        "street_name" to Arg.String(address.streetName ?: "")
                        "telephone_prefix" to Arg.String(address.telephonePrefix ?: "")
                        "landmark" to Arg.String(address.landmark ?: "")
                    }
                    Country.Sa -> {
                        "district" to Arg.String(address.district ?: "")
                    }
                    Country.Za -> {
                        "address_suburb" to Arg.String(address.addressSuburb ?: "")
                        "vat_number" to Arg.String(address.vatNumber ?: "")
                    }
                    else -> {
                        //TODO ?
                        "" to Arg.String("")
                    }
                }
            )

        sealed class AddressType {
            data class FullAddress(val address: Address) : AddressType()
            data class AddressId(val addressId: Int) : AddressType()
        }

        suspend fun setShippingAddressMutation(
            cartType: CartType,
            address: AddressType,
            token: String? = null
        ): Result<List<SdkError>, Cart?> {
            val args = when (address) {
                is AddressType.AddressId -> dictionaryOf("customer_address_id" to Arg.Int(address.addressId))
                is AddressType.FullAddress -> dictionaryOf("address" to addressArgs(address.address))
            }
            return execute<SetShippingAddressesOnCart>(
                operations = listOf(
                    mutation(
                        mutationName = "setShippingAddressesOnCart",
                        attributes = dictionaryOf(
                            "input" to dictionaryOf(
                                "cart_id" to Arg.String(cartType.id()),
                                "shipping_addresses" to Arg.Array(
                                    listOf(
                                        args
                                    )
                                )
                            )
                        )
                    ) {
                        field(cartField("cart"))
                    }
                ),
                operationType = OperationType.Mutation,
                token = token
            ).map { it?.setShippingAddressesOnCart?.cart }
        }

        suspend fun setShippingAddressAndGuestEmail(
            cartType: CartType,
            address: AddressType,
            email: String,
            token: String? = null
        ): Result<List<SdkError>, Cart?> {
            val args = when (val result = address) {
                is AddressType.AddressId -> dictionaryOf("customer_address_id" to Arg.Int(result.addressId))
                is AddressType.FullAddress -> dictionaryOf("address" to addressArgs(result.address))
            }
            return execute<SetShippingAddressesOnCart>(
                operations = listOf(
                    mutation(
                        mutationName = "setGuestEmailOnCart",
                        attributes = dictionaryOf(
                            "input" to dictionaryOf(
                                "cart_id" to Arg.String(cartType.id()),
                                "email" to Arg.String(email)
                            )
                        )
                    ) {
                        field("cart") {
                            field("id")
                        }
                    },
                    mutation(
                        mutationName = "setShippingAddressOnCart",
                        attributes = dictionaryOf(
                            "input" to dictionaryOf(
                                "cart_id" to Arg.String(cartType.id()),
                                "shipping_addresses" to Arg.Array(
                                    listOf(
                                        args
                                    )
                                )
                            )
                        )
                    ) {
                        cartField("cart")
                    }
                ),
                operationType = OperationType.Mutation,
                token = token
            ).map { it?.setShippingAddressesOnCart?.cart }
        }

        suspend fun setBillingAddress(
            cartType: CartType,
            address: AddressType,
            sameAsShipping: Boolean = false,
            token: String? = null
        ): Result<List<SdkError>, Cart?> {
            val args = when (address) {
                is AddressType.AddressId -> dictionaryOf(
                    "customer_address_id" to Arg.Int(address.addressId),
                    "same_as_shipping" to Arg.Bool(sameAsShipping)
                )
                is AddressType.FullAddress -> dictionaryOf(
                    "address" to addressArgs(address.address),
                    "same_as_shipping" to Arg.Bool(sameAsShipping)
                )
            }
            return execute<SetBillingAddressOnCart>(
                operations = listOf(
                    mutation(
                        mutationName = "setBillingAddressOnCart",
                        attributes = dictionaryOf(
                            "input" to dictionaryOf(
                                "cart_id" to Arg.String(cartType.id()),
                                "billing_address" to Arg.Array(
                                    listOf(
                                        args
                                    )
                                )
                            )
                        )
                    ) {
                        cartField("cart")
                    }
                ),
                operationType = OperationType.Mutation,
                token = token
            ).map { it?.setBillingAddressOnCart?.cart }
        }

        suspend fun setBillingAddressAndGuestEmail(
            cartType: CartType,
            address: AddressType,
            email: String,
            sameAsShipping: Boolean = false,
            token: String? = null
        ): Result<List<SdkError>, Cart?> {
            val args = when (val result = address) {
                is AddressType.AddressId -> dictionaryOf(
                    "customer_address_id" to Arg.Int(result.addressId),
                    "same_as_shipping" to Arg.Bool(sameAsShipping)
                )
                is AddressType.FullAddress -> dictionaryOf(
                    "address" to addressArgs(result.address),
                    "same_as_shipping" to Arg.Bool(sameAsShipping)
                )
            }
            return execute<SetBillingAddressOnCart>(
                operations = listOf(
                    mutation(
                        mutationName = "setGuestEmailOnCart",
                        attributes = dictionaryOf(
                            "input" to dictionaryOf(
                                "cart_id" to Arg.String(cartType.id()),
                                "email" to Arg.String(email)
                            )
                        )
                    ) {
                        field("cart") {
                            field("id")
                        }
                    },
                    mutation(
                        mutationName = "setBillingAddressOnCart",
                        attributes = dictionaryOf(
                            "input" to dictionaryOf(
                                "cart_id" to Arg.String(cartType.id()),
                                "billing_address" to Arg.Array(
                                    listOf(
                                        args
                                    )
                                )
                            )
                        )
                    ) {
                        cartField("cart")
                    }
                ),
                operationType = OperationType.Mutation,
                token = token
            ).map { it?.setBillingAddressOnCart?.cart }
        }

        suspend fun setShippingMethodAndShop(
            cartType: CartType,
            carrierCode: String,
            methodCode: String,
            shopId: String,
            token: String? = null
        ): Result<List<SdkError>, Cart?> {
            return execute<SetShopOnCart>(
                operations = listOf(
                    mutation(
                        mutationName = "setShippingMethodsOnCart",
                        attributes = dictionaryOf(
                            "input" to dictionaryOf(
                                "cart_id" to Arg.String(cartType.id()),
                                "shipping_methods" to Arg.Array(
                                    listOf(
                                        dictionaryOf(
                                            "carrier_code" to Arg.String(carrierCode),
                                            "method_code" to Arg.String(methodCode),
                                        ),
                                    )
                                )
                            )
                        )
                    ) {
                        field("cart") {
                            field("id")
                        }
                    },
                    mutation(
                        mutationName = "setShopOnCart",
                        attributes = dictionaryOf(
                            "input" to dictionaryOf(
                                "cart_id" to Arg.String(cartType.id()),
                                "shop" to Arg.String(shopId)
                            )
                        )
                    ) {
                        cartField("cart")
                    }
                ),
                operationType = OperationType.Mutation,
                token = token
            ).map { it?.setShopOnCart?.cart }
        }

        suspend fun setShippingMethodAndBillingAddressAndShop(
            cartType: CartType,
            carrierCode: String,
            methodCode: String,
            shopId: String,
            addressType: AddressType,
            token: String? = null
        ): Result<List<SdkError>, Cart?> {
            val args = when (val result = addressType) {
                is AddressType.AddressId -> dictionaryOf(
                    "customer_address_id" to Arg.Int(result.addressId)
                )
                is AddressType.FullAddress -> dictionaryOf(
                    "address" to addressArgs(result.address)
                )
            }
            return execute<SetShopOnCart>(
                operations = listOf(
                    mutation(
                        mutationName = "setShippingMethodsOnCart",
                        attributes = dictionaryOf(
                            "input" to dictionaryOf(
                                "cart_id" to Arg.String(cartType.id()),
                                "shipping_methods" to Arg.Array(
                                    listOf(
                                        dictionaryOf(
                                            "carrier_code" to Arg.String(carrierCode),
                                            "method_code" to Arg.String(methodCode),
                                        ),
                                    )
                                )
                            )
                        )
                    ) {
                        field("cart") {
                            field("id")
                        }
                    },
                    mutation(
                        mutationName = "setBillingAddressOnCart",
                        attributes = dictionaryOf(
                            "input" to dictionaryOf(
                                "cart_id" to Arg.String(cartType.id()),
                                "billing_address" to Arg.Array(
                                    listOf(
                                        args
                                    )
                                )
                            )
                        )
                    ) {
                        field("cart") {
                            field("id")
                        }
                    },
                    mutation(
                        mutationName = "setShopOnCart",
                        attributes = dictionaryOf(
                            "input" to dictionaryOf(
                                "cart_id" to Arg.String(cartType.id()),
                                "shop" to Arg.String(shopId)
                            )
                        )
                    ) {
                        cartField("cart")
                    }
                ),
                operationType = OperationType.Mutation,
                token = token
            ).map { it?.setShopOnCart?.cart }
        }

        suspend fun setPaymentMethodAndPlaceOrder(
            cartType: CartType,
            paymentMethod: String,
            token: String? = null
        ) =
            execute<SetPaymentMethodAndPlaceOrder>(
                operations = listOf(
                    mutation(
                        mutationName = "setPaymentMethodOnCart",
                        attributes = dictionaryOf(
                            "input" to dictionaryOf(
                                "cart_id" to Arg.String(cartType.id()),
                                "payment_method" to dictionaryOf(
                                    "code" to Arg.String(paymentMethod)
                                )
                            )
                        )
                    ) {
                        field("cart") {
                            field("id")
                        }
                    },
                    mutation(
                        mutationName = "placeOrder",
                        attributes = dictionaryOf(
                            "input" to dictionaryOf(
                                "cart_id" to Arg.String(cartType.id()),
                            )
                        )
                    ) {
                        field("order") {
                            field("order_number")
                            field("redirect_url")
                        }
                    }
                ),
                operationType = OperationType.Mutation,
                token = token
            ).map { it?.placeOrder?.order }
    }

    suspend fun checkoutUrl(cartType: CartType, token: String? = null) =
        execute<CheckoutUrl>(
            operations = listOf(
                query(
                    queryName = "requestCheckoutUrl",
                    attributes = dictionaryOf(
                        "input" to dictionaryOf(
                            "cart_id" to Arg.String(cartType.id()),
                        )
                    )
                ) {
                    field("redirect_url")
                }
            ),
            operationType = OperationType.Query,
            token = token
        ).map { it?.requestCheckoutUrl?.redirectUrl }

    suspend fun checkEmailAvailability(email: String, token: String? = null) =
        execute<EmailAvailability>(
            operations = listOf(
                query(
                    queryName = "isEmailAvailable",
                    attributes = dictionaryOf(
                        "email" to Arg.String("email")
                    )
                ) {
                    field("is_email_available")
                }
            ),
            operationType = OperationType.Query,
            token = token
        ).map { it?.isEmailAvailable }
}