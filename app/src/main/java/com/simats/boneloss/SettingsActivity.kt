package com.simats.boneloss

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.simats.boneloss.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: ProfileViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        observeViewModel()
        
        loadProfile()
    }

    private fun loadProfile() {
        val sharedPrefs = getSharedPreferences("user_profile", Context.MODE_PRIVATE)
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
        
        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivityForResult(intent, EDIT_PROFILE_REQUEST_CODE)
        }
        
        binding.btnNotifications.setOnClickListener {
            val intent = Intent(this, NotificationsSettingsActivity::class.java)
            startActivity(intent)
        }
        
        binding.btnPrivacy.setOnClickListener {
            val intent = Intent(this, PrivacySecurityActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogoutSection.setOnClickListener {
            getSharedPreferences("user_profile", Context.MODE_PRIVATE).edit().clear().apply()
            val intent = Intent(this, RoleSelectionActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.pbProfileLoading.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        }

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
        binding.tvUserName.text = user.fullName
        binding.tvUserEmail.text = user.email
        binding.tvUserRole.text = user.role
        
        val initials = user.fullName.split(" ")
            .filter { it.isNotEmpty() }
            .take(2)
            .map { it[0].uppercaseChar() }
            .joinToString("")
            
        binding.tvAvatarInitials.text = initials
    }
    
    override fun onResume() {
        super.onResume()
        // Refresh profile data when returning from Edit Profile or if it changes
        loadProfile()
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Profile was updated, refresh will happen in onResume
        }
    }
    
    companion object {
        private const val EDIT_PROFILE_REQUEST_CODE = 100
    }
}
