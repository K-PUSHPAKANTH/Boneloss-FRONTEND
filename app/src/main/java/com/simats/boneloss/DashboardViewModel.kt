package com.simats.boneloss

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response

class DashboardViewModel : ViewModel() {
    private val repository = DashboardRepository(ApiClient.apiService)

    private val _dashboardData = MutableLiveData<DashboardResponse?>()
    val dashboardData: LiveData<DashboardResponse?> get() = _dashboardData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun fetchDashboardData(userId: Int) {
        _isLoading.value = true
        _errorMessage.value = null
        
        viewModelScope.launch {
            try {
                android.util.Log.d("DashboardDebug", "Sending request to repository for userId: $userId")
                val response = repository.getDashboardData(userId)
                if (response.isSuccessful) {
                    val body = response.body()
                    android.util.Log.d("DashboardDebug", "Response received: $body")
                    if (body != null) {
                        _dashboardData.value = body
                    } else {
                        android.util.Log.e("DashboardDebug", "Response body is null")
                        _errorMessage.value = "Response body is empty"
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    android.util.Log.e("DashboardDebug", "API Error: ${response.code()} - $errorBody")
                    _errorMessage.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                android.util.Log.e("DashboardDebug", "Exception during API call", e)
                _errorMessage.value = "Connect Failure: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
