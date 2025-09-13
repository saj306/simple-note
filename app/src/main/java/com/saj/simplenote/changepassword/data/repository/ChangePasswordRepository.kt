package com.saj.simplenote.changepassword.data.repository

import com.google.gson.Gson
import com.saj.simplenote.changepassword.data.model.ChangePasswordRequest
import com.saj.simplenote.changepassword.data.model.ChangePasswordResponse
import com.saj.simplenote.domain.network.ApiService
import com.saj.simplenote.domain.util.Result
import com.saj.simplenote.login.data.model.ApiError

class ChangePasswordRepository(
    private val apiService: ApiService
) {

    suspend fun changePassword(request: ChangePasswordRequest): Result<ChangePasswordResponse> {
        return try {
            val response = apiService.changePassword(request)

            if (response.isSuccessful) {
                response.body()?.let { changePasswordResponse ->
                    Result.success(changePasswordResponse)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                val errorMessage = when (response.code()) {
                    400 -> {
                        // Try to parse the error response for more specific error messages
                        try {
                            response.errorBody()?.string()?.let { errorBody ->
                                val apiError = Gson().fromJson(errorBody, ApiError::class.java)
                                when {
                                    apiError.errors.any { it.attr == "old_password" } ->
                                        "Current password is incorrect."

                                    apiError.errors.any { it.attr == "new_password" } ->
                                        "New password does not meet requirements."

                                    apiError.errors.any { it.attr == "non_field_errors" } ->
                                        "Password change failed. Please check your inputs."

                                    else -> "Invalid input. Please check your passwords."
                                }
                            } ?: "Current password is incorrect."
                        } catch (e: Exception) {
                            "Current password is incorrect."
                        }
                    }

                    401 -> "Authentication failed. Please login again."
                    403 -> "You don't have permission to change password."
                    404 -> "Service not found. Please try again later."
                    500 -> "Server error. Please try again later."
                    else -> "Password change failed. Please try again."
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error. Please check your connection and try again."))
        }
    }
}
