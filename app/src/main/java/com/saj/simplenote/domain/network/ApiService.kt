package com.saj.simplenote.domain.network

import com.saj.simplenote.changepassword.data.model.ChangePasswordRequest
import com.saj.simplenote.changepassword.data.model.ChangePasswordResponse
import com.saj.simplenote.home.data.model.CreateNoteRequest
import com.saj.simplenote.home.data.model.Note
import com.saj.simplenote.home.data.model.NotesResponse
import com.saj.simplenote.login.data.model.LoginRequest
import com.saj.simplenote.login.data.model.LoginResponse
import com.saj.simplenote.login.data.model.RefreshTokenRequest
import com.saj.simplenote.login.data.model.RefreshTokenResponse
import com.saj.simplenote.register.data.model.RegisterRequest
import com.saj.simplenote.register.data.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {
    
    @POST("api/auth/token/")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    
    @POST("api/auth/token/refresh/")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<RefreshTokenResponse>
    
    @POST("api/auth/register/")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>
    
    @POST("api/auth/change-password/")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<ChangePasswordResponse>
    
    @GET("api/notes/")
    suspend fun getNotes(
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null
    ): Response<NotesResponse>
    
    @GET("api/notes/filter")
    suspend fun filterNotes(
        @Query("title") title: String? = null,
        @Query("description") description: String? = null,
        @Query("updated__gte") updatedAfter: String? = null,
        @Query("updated__lte") updatedBefore: String? = null,
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null
    ): Response<NotesResponse>
    
    @POST("api/notes/")
    suspend fun createNote(
        @Body request: CreateNoteRequest
    ): Response<Note>
    
    @GET("api/notes/{id}/")
    suspend fun getNoteById(
        @retrofit2.http.Path("id") id: Int
    ): Response<Note>
    
    @PUT("api/notes/{id}/")
    suspend fun updateNote(
        @retrofit2.http.Path("id") id: Int,
        @Body request: CreateNoteRequest
    ): Response<Note>
    
    @retrofit2.http.DELETE("api/notes/{id}/")
    suspend fun deleteNote(
        @retrofit2.http.Path("id") id: Int
    ): Response<Unit>
}
