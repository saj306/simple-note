package com.saj.simplenote.login.data.repository

import com.google.gson.Gson
import com.saj.simplenote.domain.network.ApiService
import com.saj.simplenote.login.data.model.ApiError
import com.saj.simplenote.login.data.model.LoginRequest
import com.saj.simplenote.login.data.model.LoginResponse

class LoginRepository(
    private val apiService: ApiService
) {
    
    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(username = username, password = password)
            val response = apiService.login(request)
            
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    Result.success(loginResponse)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                val errorMessage = when (response.code()) {
                    400 -> {
                        // Try to parse the error response for more specific error messages
                        try {
                            response.errorBody()?.string()?.let { errorBody ->
                                val apiError = Gson().fromJson(errorBody, ApiError::class.java)
                                when {
                                    apiError.errors.any { it.attr == "non_field_errors" } -> 
                                        "Invalid credentials. Please check your username and password."
                                    apiError.errors.any { it.attr == "username" } -> 
                                        "Invalid username format."
                                    apiError.errors.any { it.attr == "password" } -> 
                                        "Password is required."
                                    else -> "Invalid input. Please check your credentials."
                                }
                            } ?: "Invalid credentials. Please check your username and password."
                        } catch (e: Exception) {
                            "Invalid credentials. Please check your username and password."
                        }
                    }
                    401 -> "Authentication failed. Please check your credentials."
                    404 -> "Service not found. Please try again later."
                    500 -> "Server error. Please try again later."
                    else -> "Login failed. Please try again."
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error. Please check your connection and try again."))
        }
    }
}
