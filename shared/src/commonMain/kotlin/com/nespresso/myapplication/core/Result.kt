package com.nespresso.myapplication.core

import com.nespresso.myapplication.helper.DataContainer

/**
 * A value that represents either a success or a failure.
 */
sealed class Result<out Error, out Value> {
    /** A failure, storing an error. */
    data class Failure<out Error>(val error: Error) : Result<Error, Nothing>()

    /** A success, storing a value. */
    data class Success<out Value>(val value: Value) : Result<Nothing, Value>()
}

/**
 * Returns a new result, mapping any success value using the given transformation.
 */
inline fun <Error, Value, NewValue> Result<Error, Value>.map(
    transform: (value: Value) -> NewValue
): Result<Error, NewValue> = when (this) {
    is Result.Success -> Result.Success(transform(value))
    is Result.Failure -> Result.Failure(error)
}

fun <Value> DataContainer<Value>.toResult(): Result<List<SdkError>, Value?> {
    return if (!errors.isNullOrEmpty()) {
        Result.Failure(errors.map { SdkError.GqlError(it.message) })
    } else {
        Result.Success(data)
    }
}

