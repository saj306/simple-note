package com.saj.simplenote.register.data.model

data class RegisterResponse(
    val id: Int,
    val username: String,
    val email: String,
    val first_name: String,
    val last_name: String
)
