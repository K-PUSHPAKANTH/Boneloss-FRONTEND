package com.simats.boneloss

import com.google.gson.annotations.SerializedName

data class AdminDashboardResponse(
    @SerializedName("metrics") val metrics: AdminMetrics,
    @SerializedName("alerts") val alerts: List<AdminAlert>,
    @SerializedName("user_activity") val userActivity: UserActivity
)

data class AdminMetrics(
    @SerializedName("total_users") val totalUsers: Int,
    @SerializedName("total_cases") val totalCases: Int,
    @SerializedName("ai_accuracy") val aiAccuracy: Double
)

data class AdminAlert(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("type") val type: String
)

data class UserActivity(
    @SerializedName("active_users_24h") val activeUsers24h: Int
)
