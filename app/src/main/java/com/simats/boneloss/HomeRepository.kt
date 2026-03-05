package com.simats.boneloss

import retrofit2.Response

class HomeRepository(private val apiService: ApiService) {
    suspend fun getHomeData(userId: Int): Response<DashboardResponse> {
        return apiService.getDashboard(userId)
    }
}
