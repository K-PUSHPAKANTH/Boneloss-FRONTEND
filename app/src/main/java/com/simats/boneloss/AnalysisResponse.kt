package com.simats.boneloss

data class AnalysisResponse(
    val analysis_id: Int? = -1,
    val prediction: String? = null,
    val confidence: Float? = 0f,
    val average_bone_loss: Float? = 0f,
    val heatmap: List<HeatmapData>? = emptyList()
)
