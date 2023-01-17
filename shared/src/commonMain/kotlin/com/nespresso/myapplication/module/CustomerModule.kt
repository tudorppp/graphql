package com.nespresso.myapplication.module

import com.nespresso.myapplication.core.*
import com.nespresso.myapplication.entity.*
import com.nespresso.myapplication.helper.execute
import com.nespresso.myapplication.module.CatalogModule.Companion.imageField
import com.nespresso.myapplication.module.CatalogModule.Companion.productFragment

class CustomerModule {
    companion object {

        private fun customerArgs(
            customer: Customer,
            isSignup: Boolean = false,
            country: Country
        ): Arg.Dictionary {
            val subscribeEmail = customer.subscribeEmail ?: false
            val subscribeText = customer.subscribeText ?: false
            val subscribePhone = customer.subscribePhone ?: false
            val isSubscribed = subscribeEmail || subscribePhone || subscribeText

            return dictionaryOf(
                "firstname" to Arg.String(customer.firstName),
                "lastname" to Arg.String(customer.lastName),
                "email" to Arg.String(customer.email),
                "trade_phone" to Arg.String(customer.tradePhone ?: ""),
                "customercompany" to Arg.String(customer.customercompany ?: ""),
                "taxvat" to Arg.String(customer.taxvat ?: ""),
                "subscribe_email" to Arg.Bool(subscribeEmail),
                "subscribe_text" to Arg.Bool(subscribeText),
                "subscribe_phone" to Arg.Bool(subscribePhone),
                "is_subscribed" to Arg.Bool(isSubscribed),
                when (country) {
                    Country.Ae -> {
                        "customer_telephone_prefix" to Arg.String(
                            customer.customerTelephonePrefix ?: ""
                        )
                    }
                    Country.Za -> {
                        //TODO is this working?
                        "customer_account_type" to Arg.String(
                            customer.customerAccountType?.name ?: ""
                        )
                    }
                    else -> {
                        "" to Arg.String("")
                    }
                },
                if (isSignup) {
                    "password" to Arg.String(customer.password ?: "")
                } else {
                    "" to Arg.String("")
                }
            )
        }

        private fun addressArgs(
            address: Address, country: Country
        ): Arg.Dictionary {
            return dictionaryOf(
                "firstname" to Arg.String(address.firstName),
                "lastname" to Arg.String(address.lastName),
                "telephone" to Arg.String(address.telephone),
                "company" to Arg.String(address.company ?: ""),
                "city" to Arg.String(address.city),
                "street" to Arg.Array(address.street.map { Arg.String(it) }),
                "postcode" to Arg.String(address.postCode ?: ""),
                "country_code" to Arg.String(address.countryCode ?: ""),
                "default_billing" to Arg.Bool(address.defaultBilling),
                "default_shipping" to Arg.Bool(address.defaultShipping),

                when (country) {
                    Country.Ae -> {
                        "area" to Arg.String(address.area ?: "")
                        "house" to Arg.String(address.house ?: "")
                        "street_name" to Arg.String(address.streetName ?: "")
                        "telephone_prefix" to Arg.String(address.telephonePrefix ?: "")
                        "landmark" to Arg.String(address.landmark ?: "")
                    }
                    Country.Sa -> {
                        "region" to dictionaryOf(
                            "region_code" to Arg.String(address.region?.regionCode ?: ""),
                            "region_id" to dictionaryOf(
                                "region_code" to Arg.Int(
                                    address.region?.regionId ?: 0
                                )
                            ),
                            "region" to dictionaryOf(
                                "region_code" to Arg.String(
                                    address.region?.region ?: ""
                                )
                            )
                        )
                        "district" to Arg.String(address.district ?: "")
                    }
                    Country.Za -> {
                        "address_suburb" to Arg.String(address.addressSuburb ?: "")
                        "vat_number" to Arg.String(address.vatNumber ?: "")
                        "region" to dictionaryOf(
                            "region_code" to Arg.String(address.region?.regionCode ?: ""),
                            "region_id" to dictionaryOf(
                                "region_code" to Arg.Int(
                                    address.region?.regionId ?: 0
                                )
                            )
                        )
                    }
                    Country.Ma, Country.Kw -> {
                        "region" to dictionaryOf(
                            "region_code" to Arg.String(address.region?.regionCode ?: ""),
                            "region_id" to dictionaryOf(
                                "region_code" to Arg.Int(
                                    address.region?.regionId ?: 0
                                )
                            ),
                            "region" to dictionaryOf(
                                "region_code" to Arg.String(
                                    address.region?.region ?: ""
                                )
                            )
                        )
                    }
                }
            )
        }

        private fun customerQuery(wishlist: Boolean = true, country: Country = Config.country) =
            query("customer") {
                field("id")
                field("email")
                field("firstname")
                field("lastname")
                field("trade_phone")
                field("customercompany")
                field("subscribe_email")
                field("subscribe_text")
                field("subscribe_phone")
                field("taxvat")
                when (country) {
                    Country.Ae -> field("customer_telephone_prefix")
                    Country.Za -> field("customer_account_type")
                    else -> {}
                }
                field("addresses") {
                    addressField(country)
                }
                ordersField(country)
                if (wishlist) {
                    wishlistField("wishlists", country)
                }
            }

        private fun Operation.customerField(
            wishlist: Boolean = true,
            country: Country = Config.country
        ) =
            field("customer") {
                field("id")
                field("email")
                field("firstname")
                field("lastname")
                field("trade_phone")
                field("customercompany")
                field("subscribe_email")
                field("subscribe_text")
                field("subscribe_phone")
                field("taxvat")
                when (country) {
                    Country.Ae -> field("customer_telephone_prefix")
                    Country.Za -> field("customer_account_type")
                    else -> {}
                }
                field("addresses") {
                    addressField(country)
                }
                ordersField(country)
                if (wishlist) {
                    wishlistField("wishlists", country)
                }
            }

        private fun Operation.wishlistField(name: String, country: Country) =
            field(name) {
                field("items_v2") {
                    field("id")
                    productFragment("product", country)
                }
            }

        private fun Operation.addressField(country: Country) {
            field("id")
            field("firstname")
            field("lastname")
            field("telephone")
            field("company")
            field("region") {
                field("region")
                field("region_code")
                field("region_id")
            }
            field("city")
            field("street")
            field("postcode")
            field("country_code")
            field("default_billing")
            field("default_shipping")
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
        }

        private fun Operation.address(name: String, country: Country) =
            field(name) {
                field("city")
                field("company")
                field("country_code")
                field("firstname")
                field("lastname")
                field("postcode")
                field("region_id")
                field("street")
                field("telephone")
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
            }

        private fun Operation.ordersField(country: Country) {
            field("orders", dictionaryOf("pageSize" to Arg.Int(9999))) {
                field("items") {
                    address("billing_address", country)
                    field("id")
                    field("items") {
                        field("id")
                        when (country) {
                            Country.Ae -> {
                                imageField("image", "category_page_grid")
                            }
                            else -> {
                                imageField("image", null)
                            }
                        }
                        field("product_name")
                        field("product_sale_price") {
                            field("value")
                            field("currency")
                        }
                        field("product_sku")
                        field("quantity_ordered")
                        field("selected_options") {
                            field("id")
                            field("value")
                        }
                        field("number")
                        field("order_date")
                        field("payment_methods") {
                            field("name")
                            field("type")
                        }
                        address("shipping_address", country)
                        field("shipping_method")
                        field("status")
                        field("total") {
                            field("discounts") {
                                field("label")
                                field("amount") {
                                    field("value")
                                    field("currency")
                                }
                            }
                            field("total_tax") {
                                field("value")
                                field("currency")
                            }
                            field("subtotal") {
                                field("value")
                                field("currency")
                            }
                            field("grand_total") {
                                field("value")
                                field("currency")
                            }
                            field("total_shipping") {
                                field("value")
                                field("currency")
                            }
                        }
                    }
                }
            }
        }

        suspend fun registerMutation(
            customer: Customer,
            country: Country = Config.country,
            token: String? = null
        ) =
            execute<CreateCustomerContainer>(
                operations = listOf(
                    mutation(
                        mutationName = "createCustomer",
                        attributes = dictionaryOf(
                            "input" to customerArgs(customer, true, country)
                        )
                    ) {
                        customerField()
                    }
                ),
                operationType = OperationType.Mutation,
                token = token
            ).map { it?.createCustomer?.customer } //TODO still need to generate a token here

        suspend fun login(
            email: String,
            password: String,
        ): Result<List<SdkError>, Pair<Customer?, String?>> {
            return when (val newToken = IntegrationModule.newTokenMutation(email, password)) {
                is Result.Failure -> newToken
                is Result.Success -> customer(newToken.value).map { it to newToken.value }
            }
        }

        suspend fun customer(token: String? = null) =
            execute<CustomerContainer>(
                operations = listOf(
                    customerQuery()
                ),
                operationType = OperationType.Query,
                token = token
            ).map { it?.customer }

        suspend fun updatePassword(
            currentPassword: String,
            newPassword: String,
            token: String? = null
        ) {
            execute<ChangeCustomerPassword>(
                operations = listOf(
                    mutation(
                        mutationName = "changeCustomerPassword",
                        attributes = dictionaryOf(
                            "currentPassword" to Arg.String(currentPassword),
                            "newPassword" to Arg.String(newPassword),
                        )
                    ) {
                        field("email")
                    }
                ),
                operationType = OperationType.Mutation,
                token = token
            )
        }

        //TODO add resetPassword

        suspend fun resetPassword(email: String) =
            execute<Boolean>(
                method = HttpMethod.Put,
                path = "customers/password",
                body = PasswordReset(email, "email_reset")
            )


        suspend fun updateCustomerMutation(
            customer: Customer,
            country: Country = Config.country,
            token: String? = null
        ) =
            execute<UpdateCustomerContainer>(
                operations = listOf(
                    mutation(
                        mutationName = "updateCustomer",
                        attributes = dictionaryOf(
                            "input" to customerArgs(customer = customer, country = country)
                        )
                    ) {
                        customerField()
                    }
                ),
                operationType = OperationType.Mutation,
                token = token
            ).map { it?.updateCustomer?.customer }

        suspend fun createAddressMutation(
            address: Address,
            country: Country = Config.country,
            token: String? = null
        ) =
            execute<CreateCustomerAddress>(
                operations = listOf(
                    mutation(
                        mutationName = "createCustomerAddress",
                        attributes = dictionaryOf(
                            "input" to addressArgs(address, country)
                        )
                    ) {
                        addressField(country)
                    }
                ),
                operationType = OperationType.Mutation,
                token = token
            ).map { it?.createCustomerAddress }

        suspend fun updateAddressMutation(
            address: Address,
            country: Country = Config.country,
            token: String? = null
        ) =
            execute<UpdateCustomerAddress>(
                operations = listOf(
                    mutation(
                        mutationName = "updateCustomerAddress",
                        attributes = dictionaryOf(
                            "id" to Arg.Int(address.id),
                            "input" to addressArgs(address, country)
                        )
                    ) {
                        addressField(country)
                    }
                ),
                operationType = OperationType.Mutation,
                token = token
            ).map { it?.updateCustomerAddress }

        suspend fun deleteAddressMutation(
            id: Int,
            token: String? = null
        ) =
            execute<DeleteCustomerAddress>(
                operations = listOf(
                    mutation(
                        mutationName = "deleteCustomerAddress",
                        attributes = dictionaryOf(
                            "id" to Arg.Int(id),
                        )
                    )
                ),
                operationType = OperationType.Mutation,
                token = token
            ).map { it?.deleteCustomerAddress }

        suspend fun addProductsToWishlist(
            items: List<Pair<String, String?>>,
            country: Country = Config.country,
            token: String? = null
        ): Result<List<SdkError>, Wishlist?> {
            val args = items.map { (sku, parent) ->
                dictionaryOf(
                    "sku" to Arg.String(sku),
                    if (parent != null) {
                        "parent" to Arg.String(parent)
                    } else {
                        "" to Arg.String("")
                    }
                )
            }
            return execute<AddProductsToWishlist>(
                operations = listOf(
                    mutation(
                        mutationName = "addProductsToWishlist",
                        attributes = dictionaryOf(
                            "wishlistId" to Arg.String(""),
                            "wishlistItems" to Arg.Array(args)
                        )
                    ) {
                        wishlistField("wishlist", country)
                    }
                ),
                operationType = OperationType.Mutation,
                token = token
            ).map { it?.addProductsToWishlist?.wishlist }
        }

        suspend fun removeProductsFromWishList(
            ids: List<String>,
            country: Country = Config.country,
            token: String? = null
        ): Result<List<SdkError>, Wishlist?> {
            return execute<RemoveProductsFromWishlist>(
                operations = listOf(
                    mutation(
                        mutationName = "removeProductsFromWishlist",
                        attributes = dictionaryOf(
                            "wishlistId" to Arg.String(""),
                            "wishlistItemsIds" to Arg.Array(ids.map { Arg.String(it) })
                        )
                    ) {
                        wishlistField("wishlist", country)
                    }
                ),
                operationType = OperationType.Mutation,
                token = token
            ).map { it?.removeProductsFromWishlist?.wishlist }
        }
    }
}
