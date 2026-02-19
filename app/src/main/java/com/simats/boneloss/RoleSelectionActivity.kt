package com.simats.boneloss

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RoleSelectionActivity : AppCompatActivity() {

    private lateinit var cardDentist: LinearLayout
    private lateinit var cardStudent: LinearLayout
    private lateinit var cardResearcher: LinearLayout
    
    private lateinit var ivCheckDentist: ImageView
    private lateinit var ivCheckStudent: ImageView
    private lateinit var ivCheckResearcher: ImageView
    
    private lateinit var btnContinue: Button

    private var selectedRole: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_role_selection)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        cardDentist = findViewById(R.id.card_dentist)
        cardStudent = findViewById(R.id.card_student)
        cardResearcher = findViewById(R.id.card_researcher)

        ivCheckDentist = findViewById(R.id.iv_check_dentist)
        ivCheckStudent = findViewById(R.id.iv_check_student)
        ivCheckResearcher = findViewById(R.id.iv_check_researcher)

        btnContinue = findViewById(R.id.btn_continue)
    }

    private fun setupListeners() {
        cardDentist.setOnClickListener {
            selectRole("Dentist")
        }

        cardStudent.setOnClickListener {
            selectRole("Student")
        }

        cardResearcher.setOnClickListener {
            selectRole("Researcher")
        }

        btnContinue.setOnClickListener {
            if (selectedRole != null) {
                // Navigate to Login screen with selected role
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("SELECTED_ROLE", selectedRole)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select a role to continue", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectRole(role: String) {
        selectedRole = role
        
        // Update selection states for visual feedback
        cardDentist.isSelected = (role == "Dentist")
        cardStudent.isSelected = (role == "Student")
        cardResearcher.isSelected = (role == "Researcher")

        // Update checkmark visibility
        ivCheckDentist.visibility = if (role == "Dentist") View.VISIBLE else View.GONE
        ivCheckStudent.visibility = if (role == "Student") View.VISIBLE else View.GONE
        ivCheckResearcher.visibility = if (role == "Researcher") View.VISIBLE else View.GONE
    }
}
