package com.simats.boneloss

import com.google.gson.annotations.SerializedName

data class HomeResponse(
    @SerializedName("user_name") val userName: String,
    @SerializedName("total_cases") val totalCases: Int,
    @SerializedName("average_bone_loss") val averageBoneLoss: Double,
    @SerializedName("learning_modules") val learningModules: List<LearningModule>,
    @SerializedName("saved_cases") val savedCases: List<SavedCase>
)

data class LearningModule(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("is_completed") val isCompleted: Boolean
)

data class SavedCase(
    @SerializedName("title") val title: String,
    @SerializedName("bone_loss_percent") val boneLossPercent: Int,
    @SerializedName("saved_at") val savedAt: String
)
