package com.nespresso.myapplication.helper

import com.nespresso.myapplication.core.*
import io.ktor.client.plugins.*

suspend inline fun <reified T> execute(
    operations: List<Operation?>,
    operationType: OperationType,
    token: String? = null
) =
    try {
        executeGraphqlQuery<T>(
            httpClient = Http().httpClient,
            operations = operations.filterNotNull(),
            operationType = operationType,
            host = "preprod-ae.buynespresso.com",
            token = token
        )
    } catch (e: RedirectResponseException) {
        Result.Failure(listOf(SdkError.RedirectError(e.message, e.response.status.value)))
    } catch (e: ClientRequestException) {
        Result.Failure(listOf(SdkError.ClientRequestError(e.message, e.response.status.value)))
    } catch (e: ServerResponseException) {
        Result.Failure(listOf(SdkError.ServerResponseError(e.message, e.response.status.value)))
    } catch (e: Exception) {
        Result.Failure(listOf(SdkError.Unknown(e.message ?: "")))
    }

suspend inline fun <reified T> execute(method: HttpMethod, path: String, body: Any) =
    try {
        executeHttp<T>(
            httpClient = Http().httpClient,
            method,
            host = "preprod-ae.buynespresso.com/ae_en",
            path = path,
            httpBody = body
        )
    } catch (e: RedirectResponseException) {
        Result.Failure(listOf(SdkError.RedirectError(e.message, e.response.status.value)))
    } catch (e: ClientRequestException) {
        Result.Failure(listOf(SdkError.ClientRequestError(e.message, e.response.status.value)))
    } catch (e: ServerResponseException) {
        Result.Failure(listOf(SdkError.ServerResponseError(e.message, e.response.status.value)))
    } catch (e: Exception) {
        Result.Failure(listOf(SdkError.Unknown(e.message ?: "")))
    }

@kotlinx.serialization.Serializable
data class GQLErrorItem(
    val message: String
)

@kotlinx.serialization.Serializable
data class DataContainer<T>(
    val data: T? = null,
    val errors: List<GQLErrorItem>? = listOf()
) {
    fun errorDescription() = errors?.map { it.message }?.joinToString("\n\n")
}