package com.simats.boneloss

object ApiClient {

    val apiService: ApiService = RetrofitClient.retrofit.create(ApiService::class.java)
}
