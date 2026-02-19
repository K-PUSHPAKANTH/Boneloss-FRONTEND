package com.simats.boneloss

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ClinicalInterpretationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clinical_interpretation)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }

        val btnTreatment = findViewById<android.widget.Button>(R.id.btn_treatment_recommendations)
        btnTreatment.setOnClickListener {
            val intent = android.content.Intent(this, TreatmentRecommendationsActivity::class.java)
            startActivity(intent)
        }
    }
}
