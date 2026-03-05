package com.simats.boneloss

data class HistoryResponse(
    val analysis_id: Int,
    val patient_id: String,
    val prediction: String,
    val confidence: Double,
    val average_bone_loss: Double,
    val created_at: String
)
