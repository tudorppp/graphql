package com.nespresso.myapplication.core

sealed class SdkError(val message: String) {
    data class Unknown(val errorMessage: String) : SdkError(errorMessage)
    data class RedirectError(val errorMessage: String, val code: Int) : SdkError(errorMessage)
    data class ClientRequestError(val errorMessage: String, val code: Int) : SdkError(errorMessage)
    data class ServerResponseError(val errorMessage: String, val code: Int) : SdkError(errorMessage)
    data class GqlError(val errorMessage: String) : SdkError(errorMessage)
}