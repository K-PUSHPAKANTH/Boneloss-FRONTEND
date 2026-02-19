package com.simats.boneloss

import retrofit2.Response

class AuthRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return apiService.loginUser(loginRequest)
    }

    suspend fun register(registerRequest: RegisterRequest): Response<RegisterResponse> {
        return apiService.registerUser(registerRequest)
    }

    suspend fun getProfile(userId: Int): Response<User> {
        return apiService.getProfile(userId)
    }
}
