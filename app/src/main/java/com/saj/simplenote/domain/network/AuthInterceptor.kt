package com.saj.simplenote.domain.network

import com.google.gson.Gson
import com.saj.simplenote.domain.model.PreferencesManager
import com.saj.simplenote.domain.util.SharedPrefKeys
import com.saj.simplenote.login.data.model.RefreshTokenRequest
import com.saj.simplenote.login.data.model.RefreshTokenResponse
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthInterceptor(
    private val preferencesManager: PreferencesManager,
    private val baseUrl: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Add authorization header if token exists
        val accessToken = preferencesManager.getString(SharedPrefKeys.AccessToken.key, "")
        val requestWithAuth = if (accessToken.isNotEmpty() && !originalRequest.url.toString().contains("auth/token")) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        } else {
            originalRequest
        }

        val response = chain.proceed(requestWithAuth)

        // If we get 401 and have a refresh token, try to refresh
        if (response.code == 401 && !originalRequest.url.toString().contains("auth/token")) {
            val refreshToken = preferencesManager.getString(SharedPrefKeys.RefreshToken.key, "")
            
            if (refreshToken.isNotEmpty()) {
                return handleTokenRefresh(chain, originalRequest, refreshToken)
            }
        }

        return response
    }

    private fun handleTokenRefresh(chain: Interceptor.Chain, originalRequest: Request, refreshToken: String): Response {
        return runBlocking {
            try {
                // Create a temporary retrofit instance for token refresh
                val tempRetrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                
                val tempApiService = tempRetrofit.create(ApiService::class.java)
                
                // Make refresh token request
                val refreshRequest = RefreshTokenRequest(refresh = refreshToken)
                val refreshResponse = tempApiService.refreshToken(refreshRequest)
                
                if (refreshResponse.isSuccessful) {
                    val newTokenResponse = refreshResponse.body()
                    if (newTokenResponse != null) {
                        // Store the new access token
                        preferencesManager.putString(SharedPrefKeys.AccessToken.key, newTokenResponse.access)
                        
                        // Retry the original request with new token
                        val newRequest = originalRequest.newBuilder()
                            .header("Authorization", "Bearer ${newTokenResponse.access}")
                            .build()
                        
                        return@runBlocking chain.proceed(newRequest)
                    }
                } else {
                    // Refresh failed, clear tokens and redirect to login
                    clearTokens()
                }
            } catch (e: Exception) {
                // Refresh failed, clear tokens
                clearTokens()
            }
            
            // If refresh failed, return original 401 response
            chain.proceed(originalRequest)
        }
    }

    private fun clearTokens() {
        preferencesManager.remove(SharedPrefKeys.AccessToken.key)
        preferencesManager.remove(SharedPrefKeys.RefreshToken.key)
        preferencesManager.remove(SharedPrefKeys.Username.key)
        preferencesManager.remove(SharedPrefKeys.Password.key)
    }
}
