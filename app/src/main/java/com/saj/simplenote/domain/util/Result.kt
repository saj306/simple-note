package com.saj.simplenote.domain.util

/**
 * A sealed class representing the result of an operation.
 * Either Success with a value, or Failure with an exception.
 */
sealed class Result<out T> {
    data class Success<out T>(val value: T) : Result<T>()
    data class Failure(val exception: Exception) : Result<Nothing>()

    inline fun <R> fold(
        onSuccess: (value: T) -> R,
        onFailure: (exception: Exception) -> R
    ): R = when (this) {
        is Success -> onSuccess(value)
        is Failure -> onFailure(exception)
    }

    fun isSuccess(): Boolean = this is Success
    fun isFailure(): Boolean = this is Failure

    fun getOrNull(): T? = when (this) {
        is Success -> value
        is Failure -> null
    }

    fun exceptionOrNull(): Exception? = when (this) {
        is Success -> null
        is Failure -> exception
    }

    companion object {
        fun <T> success(value: T): Result<T> = Success(value)
        fun failure(exception: Exception): Result<Nothing> = Failure(exception)
    }
}
