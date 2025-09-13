package com.saj.simplenote.register.data.repository

import com.google.gson.Gson
import com.saj.simplenote.domain.network.ApiService
import com.saj.simplenote.login.data.model.ApiError
import com.saj.simplenote.register.data.model.RegisterRequest
import com.saj.simplenote.register.data.model.RegisterResponse

class RegisterRepository(
    private val apiService: ApiService
) {
    
    suspend fun register(
        username: String,
        password: String,
        email: String,
        firstName: String,
        lastName: String
    ): Result<RegisterResponse> {
        return try {
            val request = RegisterRequest(
                username = username,
                password = password,
                email = email,
                first_name = firstName,
                last_name = lastName
            )
            val response = apiService.register(request)
            
            if (response.isSuccessful) {
                response.body()?.let { registerResponse ->
                    Result.success(registerResponse)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                val errorMessage = when (response.code()) {
                    400 -> {
                        try {
                            val errorBody = response.errorBody()?.string()
                            val apiError = Gson().fromJson(errorBody, ApiError::class.java)
                            val errorDetails = apiError.errors.joinToString(", ") { it.detail }
                            "Registration failed: $errorDetails"
                        } catch (e: Exception) {
                            "Registration failed. Please check your information and try again."
                        }
                    }
                    409 -> "Username or email already exists. Please try with different credentials."
                    500 -> "Server error. Please try again later."
                    else -> "Registration failed. Please try again."
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error. Please check your connection and try again."))
        }
    }
}
