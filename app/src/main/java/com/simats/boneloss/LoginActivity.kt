package com.simats.boneloss

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.simats.boneloss.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private var isPasswordVisible = false
    private var selectedRole: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get selected role from Intent
        selectedRole = intent.getStringExtra("SELECTED_ROLE")

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        // Password visibility toggle
        binding.ivShowPassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.ivShowPassword.setImageResource(R.drawable.ic_visibility_off)
            } else {
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.ivShowPassword.setImageResource(R.drawable.ic_visibility)
            }
            binding.etPassword.setSelection(binding.etPassword.text.length)
        }

        // Forgot Password
        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Forgot password clicked", Toast.LENGTH_SHORT).show()
        }

        // Sign Up button - Navigate to Register screen
        binding.btnGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val loginRequest = LoginRequest(email, password)
                viewModel.loginUser(loginRequest)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            // You could show a progress bar here
            binding.btnLogin.isEnabled = !isLoading
        }

        viewModel.loginResponse.observe(this) { response ->
            if (response.isSuccessful && response.body()?.status == "success") {
                val user = response.body()?.user
                
                // Save user profile to SharedPreferences
                val sharedPrefs = getSharedPreferences("user_profile", MODE_PRIVATE)
                sharedPrefs.edit().apply {
                    putString("role", selectedRole ?: user?.role ?: "dentist")
                    putString("full_name", user?.fullName)
                    putString("email", user?.email)
                    putInt("user_id", user?.id ?: -1)
                    apply()
                }
                
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                
                val finalRole = (selectedRole ?: user?.role ?: "dentist").lowercase()
                
                // Navigate based on role
                val nextActivity = when {
                    finalRole.contains("student") -> StudentDashboardActivity::class.java
                    finalRole.contains("admin") -> AdminDashboardActivity::class.java
                    finalRole.contains("researcher") -> ResearcherDashboardActivity::class.java
                    else -> HomeActivity::class.java
                }
                
                val intent = Intent(this, nextActivity)
                intent.putExtra("user_id", user?.id ?: -1)
                startActivity(intent)
                finish()
            } else {
                val errorMsg = response.body()?.message ?: "Login failed: ${response.code()}"
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.errorMessage.observe(this) { error ->
            Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
        }
    }
}
