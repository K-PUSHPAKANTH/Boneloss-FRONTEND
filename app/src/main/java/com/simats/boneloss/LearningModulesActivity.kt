package com.simats.boneloss

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class LearningModulesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning_modules)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnContinue = findViewById<android.widget.Button>(R.id.btn_continue_radiographic)

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnContinue?.setOnClickListener {
            val intent = Intent(this, RadiographicInterpretationActivity::class.java)
            startActivity(intent)
        }
    }
}
