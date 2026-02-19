package com.simats.boneloss

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class DistributionAnalysisActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distribution_analysis)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnSeverityClassification = findViewById<android.widget.Button>(R.id.btn_severity_classification)

        btnBack.setOnClickListener {
            finish()
        }

        btnSeverityClassification.setOnClickListener {
            val intent = android.content.Intent(this, SeverityClassificationActivity::class.java)
            startActivity(intent)
        }
    }
}
