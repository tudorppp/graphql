package com.nespresso.myapplication

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class AndroidPlatform : Platform {
    override val name: String = "Android"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun initLogger() {
    Napier.base(DebugAntilog())
}