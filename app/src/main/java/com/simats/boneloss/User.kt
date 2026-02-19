package com.simats.boneloss

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    @SerializedName("full_name") val fullName: String,
    val email: String,
    val role: String,
    val phone: String? = null,
    val institution: String? = null,
    val license: String? = null
)
