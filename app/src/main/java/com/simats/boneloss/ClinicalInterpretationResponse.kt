package com.simats.boneloss

data class ClinicalInterpretationResponse(
    val prediction: String? = null,
    val average_bone_loss: Float? = 0f,
    val primary_diagnosis: String? = null,
    val icd_10: String? = null,
    val stage: String? = null,
    val grade: String? = null,
    val extent: String? = null,
    val clinical_summary: String? = null,
    val clinical_findings: String? = null
)
