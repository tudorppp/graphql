package com.nespresso.myapplication.entity

@kotlinx.serialization.Serializable
data class PasswordReset(
    val email: String,
    val template: String
)
