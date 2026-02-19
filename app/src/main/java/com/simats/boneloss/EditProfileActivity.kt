package com.simats.boneloss

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import androidx.activity.viewModels
import com.simats.boneloss.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        observeViewModel()
        
        loadProfile()
    }

    private fun loadProfile() {
        val sharedPrefs = getSharedPreferences("user_profile", MODE_PRIVATE)
        val userId = sharedPrefs.getInt("user_id", -1)
        
        if (userId != -1) {
            viewModel.fetchProfile(userId)
        } else {
            Toast.makeText(this, "User session expired", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSave.setOnClickListener {
            saveProfileData()
        }

        // Navigation Listeners
        binding.navHome.setOnClickListener {
            val intent = Intent(this, ResearcherDashboardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        binding.navAnalytics.setOnClickListener {
            val intent = Intent(this, AnalyticsActivity::class.java)
            startActivity(intent)
        }

        binding.navFabContainer.setOnClickListener {
            val intent = Intent(this, PatientHistoryActivity::class.java)
            startActivity(intent)
        }

        binding.navSecurity.setOnClickListener {
            val intent = Intent(this, PrivacySecurityActivity::class.java)
            startActivity(intent)
        }

        binding.navProfile.setOnClickListener {
            // Already on Profile
        }
    }

    private fun observeViewModel() {
        viewModel.profileResponse.observe(this) { response ->
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!
                updateUI(user)
            }
        }

        viewModel.errorMessage.observe(this) { error ->
            Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(user: User) {
        binding.etFullName.setText(user.fullName)
        binding.etEmail.setText(user.email)
        binding.etPhone.setText(user.phone ?: "")
        binding.etInstitution.setText(user.institution ?: "")
        binding.etLicense.setText(user.license ?: "")
        
        updateAvatarInitials(user.fullName)
    }

    private fun saveProfileData() {
        val fullName = binding.etFullName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val institution = binding.etInstitution.text.toString().trim()
        val license = binding.etLicense.text.toString().trim()

        if (fullName.isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            return
        }

        // Ideally, we should have an API call to update the profile here.
        // For now, we save to SharedPreferences and inform the user.
        // In a real app, viewModel.updateProfile(...) would be called.
        
        val sharedPrefs = getSharedPreferences("user_profile", MODE_PRIVATE)
        sharedPrefs.edit().apply {
            putString("full_name", fullName)
            putString("email", email)
            putString("phone", phone)
            putString("institution", institution)
            putString("license", license)
            apply()
        }

        Toast.makeText(this, "Profile saved locally (API update pending)", Toast.LENGTH_SHORT).show()
        setResult(RESULT_OK)
        finish()
    }

    private fun updateAvatarInitials(fullName: String) {
        val initials = fullName.split(" ")
            .take(2)
            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
            .joinToString("")
        binding.tvAvatarInitials.text = if (initials.isNotEmpty()) initials else "U"
    }
}
