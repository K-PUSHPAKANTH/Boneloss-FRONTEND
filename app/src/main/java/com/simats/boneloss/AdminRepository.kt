package com.simats.boneloss

import retrofit2.Response

class AdminRepository(private val apiService: ApiService) {
    suspend fun getAdminDashboard(): Response<AdminDashboardResponse> {
        return apiService.getAdminDashboard()
    }
}
