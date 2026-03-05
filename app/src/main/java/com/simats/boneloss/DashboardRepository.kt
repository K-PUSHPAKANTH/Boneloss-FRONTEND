package com.simats.boneloss

import retrofit2.Response

class DashboardRepository(private val apiService: ApiService) {
    suspend fun getDashboardData(userId: Int): Response<DashboardResponse> {
        return apiService.getDashboard(userId)
    }
}
