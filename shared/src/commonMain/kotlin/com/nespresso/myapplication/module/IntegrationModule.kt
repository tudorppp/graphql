package com.nespresso.myapplication.module

import com.nespresso.myapplication.core.*
import com.nespresso.myapplication.helper.execute

class IntegrationModule {
    companion object {
        suspend fun newTokenMutation(username: String, password: String, token: String? = null) =
            execute<GenerateCustomerToken>(
                operations = listOf(
                    mutation(
                        mutationName = "generateCustomerToken",
                        attributes = dictionaryOf(
                            "email" to Arg.String(username),
                            "password" to Arg.String(password)
                        )
                    ) {
                        field("token")
                    }
                ),
                operationType = OperationType.Mutation,
                token = token
            ).map { it?.generateCustomerToken?.token }
    }
}

@kotlinx.serialization.Serializable
data class GenerateCustomerToken(
    val generateCustomerToken: Token? = null
) {
    @kotlinx.serialization.Serializable
    data class Token(
        val token: String
    )
}