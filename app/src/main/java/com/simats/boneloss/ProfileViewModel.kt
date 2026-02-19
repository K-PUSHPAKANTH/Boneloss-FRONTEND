package com.simats.boneloss

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response

class ProfileViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    private val _profileResponse = MutableLiveData<Response<User>>()
    val profileResponse: LiveData<Response<User>> = _profileResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchProfile(userId: Int) {
        _isLoading.value = true
        android.util.Log.d("ProfileViewModel", "Fetching profile for userId: $userId")
        viewModelScope.launch {
            try {
                val response = repository.getProfile(userId)
                _profileResponse.value = response
                if (response.isSuccessful) {
                    android.util.Log.d("ProfileViewModel", "Profile fetch successful: ${response.body()}")
                } else {
                    android.util.Log.e("ProfileViewModel", "Profile fetch failed with code: ${response.code()}")
                    _errorMessage.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                android.util.Log.e("ProfileViewModel", "Profile fetch error: ${e.message}")
                _errorMessage.value = e.message ?: "An unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
