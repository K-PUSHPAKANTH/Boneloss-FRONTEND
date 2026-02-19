package com.simats.boneloss

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CompleteProfileActivity : AppCompatActivity() {

    private lateinit var cardDentist: LinearLayout
    private lateinit var cardStudent: LinearLayout
    private lateinit var cardResearcher: LinearLayout
    
    private lateinit var ivCheckDentist: ImageView
    private lateinit var ivCheckStudent: ImageView
    private lateinit var ivCheckResearcher: ImageView
    
    private lateinit var etInstitution: EditText
    private lateinit var etLicense: EditText
    private lateinit var btnCompleteSetup: Button

    private var selectedRole = "Dentist"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_profile)

        initViews()
        setupListeners()
        updateSelection("Dentist")
    }

    private fun initViews() {
        cardDentist = findViewById(R.id.card_dentist)
        cardStudent = findViewById(R.id.card_student)
        cardResearcher = findViewById(R.id.card_researcher)

        ivCheckDentist = findViewById(R.id.iv_check_dentist)
        ivCheckStudent = findViewById(R.id.iv_check_student)
        ivCheckResearcher = findViewById(R.id.iv_check_researcher)

        etInstitution = findViewById(R.id.et_institution)
        etLicense = findViewById(R.id.et_license)
        btnCompleteSetup = findViewById(R.id.btn_complete_setup)
    }

    private fun setupListeners() {
        cardDentist.setOnClickListener {
            updateSelection("Dentist")
        }

        cardStudent.setOnClickListener {
            updateSelection("Student")
        }

        cardResearcher.setOnClickListener {
            updateSelection("Researcher")
        }

        btnCompleteSetup.setOnClickListener {
            // Get the profile data
            val institution = etInstitution.text.toString().trim()
            val license = etLicense.text.toString().trim()
            
            // Save profile data to SharedPreferences
            val sharedPrefs = getSharedPreferences("user_profile", Context.MODE_PRIVATE)
            sharedPrefs.edit().apply {
                putString("role", selectedRole)
                putString("institution", institution)
                putString("license", license)
                apply()
            }
            
            // Show success message
            Toast.makeText(this, "Profile saved successfully", Toast.LENGTH_SHORT).show()
            
            // Navigate to appropriate dashboard based on role
            val intent = when (selectedRole) {
                "Dentist" -> Intent(this, ClinicianDashboardActivity::class.java)
                "Student" -> Intent(this, StudentDashboardActivity::class.java)
                else -> Intent(this, ResearcherDashboardActivity::class.java)
            }
            startActivity(intent)
            finish()
        }
    }

    private fun updateSelection(role: String) {
        selectedRole = role
        
        // Update selection states for visual feedback (handled by bg_role_selection selector)
        cardDentist.isSelected = (role == "Dentist")
        cardStudent.isSelected = (role == "Student")
        cardResearcher.isSelected = (role == "Researcher")

        // Update checkmark visibility
        ivCheckDentist.visibility = if (role == "Dentist") View.VISIBLE else View.GONE
        ivCheckStudent.visibility = if (role == "Student") View.VISIBLE else View.GONE
        ivCheckResearcher.visibility = if (role == "Researcher") View.VISIBLE else View.GONE
        
        // Note: In a real app, you might also change text colors or icon tints here
    }
}
