package com.simats.boneloss

import com.google.gson.annotations.SerializedName

data class PatientReport(
    @SerializedName("patient_id") val patientId: String?,
    @SerializedName("report_date") val reportDate: String?,
    @SerializedName("overall_status") val overallStatus: String?,
    @SerializedName("severity") val severity: String?,
    @SerializedName("average_bone_loss") val averageBoneLoss: Double?,
    @SerializedName("executive_summary") val executiveSummary: String?,
    @SerializedName("primary_diagnosis") val primaryDiagnosis: String?,
    @SerializedName("affected_teeth") val affectedTeeth: List<String>?,
    @SerializedName("risk_assessment") val riskAssessment: String?,
    @SerializedName("tooth_analytics") val toothAnalytics: List<ToothAnalytic>?
)

data class ToothAnalytic(
    @SerializedName("tooth") val tooth: Int?,
    @SerializedName("bone_loss_percent") val boneLossPercent: Double?,
    @SerializedName("root_length_mm") val rootLengthMm: Double?,
    @SerializedName("bone_loss_mm") val boneLossMm: Double?
)
