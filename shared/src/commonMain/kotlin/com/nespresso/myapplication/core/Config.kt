package com.nespresso.myapplication.core

import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object Config {

    var url: String = ""

    var store: String = "app_en"

    var country: Country = Country.Ae

    var appVersion: String = ""

    fun setupConfig(url: String, store: String, country: Country, appVersion: String) {
        Config.url = url
        Config.store = store
        Config.country = country
        Config.appVersion = appVersion
    }

}

enum class Country(val representation: String) {
    Ae("AE"), Sa("SA"), Za("ZA"), Kw("KW"), Ma("MA")
}