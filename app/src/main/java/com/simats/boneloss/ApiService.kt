package com.simats.boneloss

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.GET

interface ApiService {

    @Multipart
    @POST("/predict")
    suspend fun uploadImage(@Part file: MultipartBody.Part): Response<PredictionResponse>

    @POST("/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @GET("/profile/{user_id}")
    suspend fun getProfile(@Path("user_id") userId: Int): Response<User>
}
