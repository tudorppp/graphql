package com.nespresso.myapplication.module

import com.nespresso.myapplication.core.*
import com.nespresso.myapplication.entity.*
import com.nespresso.myapplication.helper.execute

class AdditionalModule {
    companion object {
        suspend fun boutiquesQuery(countryId: String, token: String? = null) =
            execute<BoutiqueContainer>(
                operations = listOf(
                    query(
                        queryName = "shopFinder",
                        attributes = dictionaryOf("country_id" to Arg.String(countryId))
                    ) {
                        field("items") {
                            field("can_collect")
                            field("city")
                            field("country_id")
                            field("identifier")
                            field("latitude")
                            field("longitude")
                            field("other_opentimes_info")
                            field("postcode")
                            field("region")
                            field("shop_email")
                            field("shop_name")
                            field("street")
                            field("telephone")
                        }
                    }
                ),
                operationType = OperationType.Query,
                token = token
            ).map { it?.shopFinder?.items }

        suspend fun cmsBlocksQuery(ids: List<String>, token: String? = null) =
            execute<CmsBlocksContainer>(
                operations = listOf(
                    query(
                        queryName = "cmsBlocks",
                        attributes = dictionaryOf("identifiers" to Arg.Array(ids.map { Arg.String(it) }))
                    ) {
                        field("items") {
                            field("identifier")
                            field("title")
                            field("content")
                        }
                    }
                ),
                operationType = OperationType.Query,
                token = token
            ).map { it?.cmsBlocks?.items }

        suspend fun storeConfigQuery() =
            execute<StoreConfigContainer>(
                operations = listOf(
                    query(
                        queryName = "storeConfig"
                    ) {
                        field("checkout_cities")
                        field("guest_checkout")
                        field("minimum_order_active")
                        field("minimum_order_amount")
                        field("minimum_order_text")
                    }
                ),
                operationType = OperationType.Query
            ).map { it?.storeConfig }

        suspend fun areaFinderQuery(token: String? = null) =
            execute<AreaContainer>(
                operations = listOf(
                    query(
                        queryName = "areaFinder"
                    ) {
                        field("items") {
                            field("area")
                            field("city")
                        }
                    }
                ),
                operationType = OperationType.Query,
                token = token
            ).map { it?.areaFinder?.items }

        //TODO this also takes a city? do we need to add it?
        suspend fun districtFinderQuery(token: String? = null) =
            execute<DistrictContainer>(
                operations = listOf(
                    query(
                        queryName = "districtFinder"
                    ) {
                        field("items") {
                            field("district_en")
                            field("city_en")
                            field("state_en")
                            field("district_ar")
                            field("city_ar")
                            field("state_ar")
                            field("state_id")
                        }
                    }
                ),
                operationType = OperationType.Query,
                token = token
            ).map { it?.districtFinder?.items }

        suspend fun regionsQuery(
            country: String = Config.country.representation,
            token: String? = null
        ) =
            execute<CountryContainer>(
                operations = listOf(
                    query(
                        queryName = "country",
                        attributes = dictionaryOf("id" to Arg.String(country))
                    ) {
                        field("id")
                        field("full_name_locale")
                        field("available_regions") {
                            field("code")
                            field("name")
                            field("id")
                        }
                    }
                ),
                operationType = OperationType.Query,
                token = token
            ).map { it?.country?.availableRegions }

        suspend fun currencyQuery(token: String? = null) =
            execute<CurrencyContainer>(
                operations = listOf(
                    query(
                        queryName = "currency"
                    ) {
                        field("base_currency_symbol")
                        field("base_currency_code")
                    }
                ),
                operationType = OperationType.Query,
                token = token
            ).map { it?.currency }

        suspend fun telephonePrefixQuery(token: String? = null) =
            execute<CustomAttributeMetadataContainer>(
                operations = listOf(
                    query(
                        queryName = "customAttributeMetadata",
                        dictionaryOf(
                            "attributes" to dictionaryOf(
                                "attribute_code" to Arg.String("telephone_prefix"),
                                "entity_type" to Arg.String("customer_address")
                            )
                        )
                    ) {
                        field("items") {
                            field("attribute_options") {
                                field("label")
                                field("value")
                            }
                        }
                    }
                ),
                operationType = OperationType.Query,
                token = token
            ).map { it?.customAttributeMetadata?.items }
    }
}