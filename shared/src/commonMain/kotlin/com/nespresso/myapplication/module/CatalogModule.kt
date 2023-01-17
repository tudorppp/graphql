package com.nespresso.myapplication.module

import com.nespresso.myapplication.core.*
import okio.ByteString.Companion.encodeUtf8

class CatalogModule {
    companion object {
        suspend fun categoryListQuery(ids: List<Int>, token: String? = null) =
            com.nespresso.myapplication.helper.execute<Any>(
                listOf(
                    query(
                        queryName = "categoryList",
                        attributes = dictionaryOf(
                            "filters" to dictionaryOf(
                                "ids" to dictionaryOf(
                                    "in " to Arg.Array(ids.map { Arg.Int(it) })
                                )
                            )
                        )
                    ) {
                        field("id")
                        field("name")
                        field("position")
                        field("children") {
                            field("id")
                            field("name")
                            field("position")
                        }
                    },
                    query(
                        queryName = "categoryList",
                        attributes = dictionaryOf(
                            "filters" to dictionaryOf(
                                "ids" to dictionaryOf(
                                    "in " to Arg.Array(ids.map { Arg.Int(it) })
                                )
                            )
                        )
                    ) {
                        field("id")
                        field("name")
                        field("position")
                        field("children") {
                            field("id")
                            field("name")
                            field("position")
                        }
                    }
                ),
                operationType = OperationType.Query,
                token = token
            )

        suspend fun productItemsQuery(
            categoryIds: List<Int>,
            attributeCodes: List<String>,
            token: String? = null
        ) {
            com.nespresso.myapplication.helper.execute<Any>(
                listOf(
                    query(
                        queryName = "products",
                        attributes = dictionaryOf(
                            "filter" to productsFilter(categoryIds),
                            "pageSize" to Arg.Int(1000000)
                        )
                    ) {
                        productFragment(
                            name = "items",
                            country = Config.country,
                            children = listOf(productFragment("upsell_products", Config.country))
                        )
                    },
                    query(
                        queryName = "customAttributeMetadata",
                        attributes = dictionaryOf(
                            "attributes" to Arg.Array(
                                attributeCodes.map {
                                    dictionaryOf(
                                        "attribute_code" to Arg.String(it),
                                        "entity_type" to Arg.String("10")
                                    )
                                }
                            )
                        )
                    ) {
                        field("items") {
                            field("attribute_code")
                            field("attribute_type")
                            field("attribute_options") {
                                field("label")
                                field("value")
                                field("swatch_data") {
                                    field("value")
                                }
                            }
                        }
                    }
                ),
                operationType = OperationType.Query,
                token = token
            )
        }

        suspend fun product(search: String, categoryIds: List<Int>, token: String? = null) {
            com.nespresso.myapplication.helper.execute<Any>(
                listOf(
                    query(
                        queryName = "products",
                        attributes = dictionaryOf(
                            "search" to Arg.String(search),
                            "filter" to productsFilter(categoryIds),
                            "pageSize" to Arg.Int(1000000)
                        )
                    ) {
                        productFragment("items", country = Config.country)
                    }
                ),
                operationType = OperationType.Query,
                token = token
            )
        }

        private fun productsFilter(categoryIds: List<Int>, country: Country = Config.country)
                : Arg.Dictionary {
            return when (country) {
                Country.Ae, Country.Kw -> {
                    dictionaryOf(
                        "category_uid" to dictionaryOf(
                            "in" to Arg.Array(
                                categoryIds.map {
                                    Arg.String(it.toString().encodeUtf8().base64())
                                })
                        )
                    )
                }
                else -> {
                    dictionaryOf(
                        "category_id" to dictionaryOf(
                            "in" to Arg.Array(categoryIds.map { Arg.Int(it) })
                        )
                    )
                }
            }
        }


        fun Operation.imageField(name: String, resizedId: String? = null): Field {
            return field(name) {
                field("url")
                field("disabled")
                resizedId?.let {
                    field("resized", dictionaryOf("image_ids" to Arg.String(it)))
                }
            }
        }

        private fun Operation.priceRangeField(): Field {
            return field("price_range") {
                field("minimum_price") {
                    field("discount") {
                        field("amount_off")
                    }
                    field("final_price")
                }
            }
        }

        fun Operation.productFragment(
            name: String,
            country: Country,
            children: List<Field> = listOf()
        ): Field {
            return field(name) {
                field("__typename")
                field("id")
                field("sku")
                field("name")
                field("intensity")
                field("aromatic_profile")
                field("aromatic_filter")
                field("new_from_date")
                if (country in listOf(Country.Ae, Country.Sa)) {
                    field("vertuo_cup_size")
                    field("coffee_aromatic_profile")
                }
                field("cup_size_dup")
                field("body")
                field("bitterness")
                field("acidity")
                field("roasting")
                field("roasting_label")
                field("origin")
                field("is_milk")
                field("machine_coffee_cups")
                field("machine_dimensions")
                field("coffee_volume_control")
                field("mac_capsule_container_capacity")
                field("machine_preheating_time")
                field("machine_water_tank")
                field("pressure_pump")
                field("stock_status")
                inlineFragmentPartially("PhysicalProductInterface") {
                    field("weight")
                }
                field("description") {
                    field("html")
                }
                field("short_description") {
                    field("html")
                }
                if (country == Country.Ae) {
                    imageField("thumbnail", "category_page_grid")
                    imageField("media_gallery", "product_page_image_medium")
                } else {
                    imageField(name = "thumbnail", resizedId = null)
                    imageField(name = "media_gallery", resizedId = null)
                }
                field("categories") {
                    field("id")
                }
                priceRangeField()
                inlineFragmentPartially("ConfigurableProduct") {
                    field("variants") {
                        field("product") {
                            field("name")
                            field("sku")
                            field("stock_status")
                            if (country == Country.Ae) {
                                imageField("media_gallery", "product_page_image_medium")
                            } else {
                                imageField("media_gallery")
                            }
                            priceRangeField()
                        }
                        field("attributes") {
                            field("code")
                            field("value_index")
                        }
                    }
                    field("configurable_options") {
                        field("values") {
                            field("value_index")
                            field("swatch_data") {
                                field("value")
                            }
                        }
                    }
                }
                inlineFragmentPartially("BundleProduct") {
                    field("items") {
                        field("option_id")
                        field("type")
                        field("options") {
                            field("id")
                            field("quantity")
                            field("position")
                            field("product") {
                                field("name")
                                if (country == Country.Ae) {
                                    imageField("thumbnail", "category_page_grid")
                                } else {
                                    imageField("thumbnail")
                                }
                            }
                        }
                    }
                }
                children.forEach {
                    field(it)
                }
            }
        }
    }
}
