package com.nespresso.myapplication.core

import com.nespresso.myapplication.Query
import com.nespresso.myapplication.getPlatform
import com.nespresso.myapplication.helper.DataContainer
import com.nespresso.myapplication.initLogger
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

enum class HttpMethod {
    Get, Post, Put, Delete
}

class Http {
    val httpClient = HttpClient {
        expectSuccess = true
        install(ContentNegotiation) {
            json(
                json = Json {
                    encodeDefaults = true
                    isLenient = true
                    allowSpecialFloatingPointValues = true
                    allowStructuredMapKeys = true
                    prettyPrint = false
                    useArrayPolymorphism = false
                    ignoreUnknownKeys = true
                }
            )
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.v(tag = "Http Client", message = message)
                }
            }
        }
    }.also {
        initLogger()
    }
}

suspend inline fun <reified T> executeGraphqlQuery(
    httpClient: HttpClient,
    operations: List<Operation>,
    operationType: OperationType? = null,
    token: String? = null,
    host: String,
    store: String = Config.store,
    appVersion: String = Config.appVersion
) = httpClient.post {
    headers {
        append("Store", store)
        append("Content-Type", "application/json")
        append("User-Agent", "${getPlatform().name}/$appVersion")
    }
    val queries = "${operationType?.name} { ${operations.joinToString("")} }"

    url {
        this.protocol = URLProtocol.HTTPS
        this.host = "$host/graphql/"
    }
    setBody(Query(queries))
    method = io.ktor.http.HttpMethod.Post
    token?.let {
        headers {
            append("Authorization", "Bearer $it")
        }
    }
}.body<DataContainer<T>>().toResult()

suspend inline fun <reified T> executeHttp(
    httpClient: HttpClient,
    httpMethod: HttpMethod,
    httpBody: Any? = null,
    token: String? = null,
    path: String = "",
    store: String = Config.store,
    appVersion: String = Config.appVersion,
    host: String
) = when (httpMethod) {
    HttpMethod.Get -> {
        httpClient.get {
            this.populateRequestBuilderForHttp(
                store, appVersion, path, httpBody, token, host
            )
        }.body<T>()
    }
    HttpMethod.Post -> {
        httpClient.post {
            this.populateRequestBuilderForHttp(
                store, appVersion, path, httpBody, token, host
            )
        }.body<T>()
    }
    HttpMethod.Put -> {
        httpClient.put {
            this.populateRequestBuilderForHttp(
                store, appVersion, path, httpBody, token, host
            )
        }.body<T>()
    }
    HttpMethod.Delete -> {
        httpClient.delete {
            this.populateRequestBuilderForHttp(
                store, appVersion, path, httpBody, token, host
            )
        }.body<T>()
    }
}

fun HttpRequestBuilder.populateRequestBuilderForHttp(
    store: String = Config.store,
    appVersion: String = Config.appVersion,
    path: String,
    httpBody: Any? = null,
    token: String? = null,
    host: String
): HttpRequestBuilder {
    headers {
        append("Store", store)
        append("Content-Type", "application/json")
        append("User-Agent", "${getPlatform().name}/$appVersion")
    }
    url {
        this.protocol = URLProtocol.HTTPS
        this.host = "${host}/rest/$store/V1"
        this.path(path)
    }
    httpBody?.let {
        setBody(it)
    }
    token?.let {
        headers {
            append("Authorization", "Bearer $it")
        }
    }
    return this
}