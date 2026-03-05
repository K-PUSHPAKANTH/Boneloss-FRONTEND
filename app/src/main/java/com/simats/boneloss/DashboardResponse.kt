package com.simats.boneloss

import com.google.gson.annotations.SerializedName

data class DashboardResponse(
    val user_name: String?,
    val total_cases: Int?,
    val average_confidence: Double?
)

data class RecentActivityItem(
    @SerializedName("patient_id") val patientId: String,
    @SerializedName("bone_loss_percent") val boneLossPercent: Int,
    @SerializedName("status") val status: String
)
