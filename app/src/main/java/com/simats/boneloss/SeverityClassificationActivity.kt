package com.simats.boneloss

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SeverityClassificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_severity_classification)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnToothBreakdown = findViewById<android.widget.Button>(R.id.btn_tooth_breakdown)

        btnBack.setOnClickListener {
            finish()
        }

        btnToothBreakdown.setOnClickListener {
            val intent = android.content.Intent(this, ClinicalInterpretationActivity::class.java)
            startActivity(intent)
        }
    }
}
