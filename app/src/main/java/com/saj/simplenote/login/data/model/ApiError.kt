package com.saj.simplenote.login.data.model

data class ApiError(
    val type: String,
    val errors: List<ErrorDetail>
)

data class ErrorDetail(
    val attr: String,
    val code: String,
    val detail: String
)
