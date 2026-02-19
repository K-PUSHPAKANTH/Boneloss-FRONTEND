package com.simats.boneloss

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class TreatmentRecommendationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment_recommendations)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }

        val btnReportSummary = findViewById<android.widget.Button>(R.id.btn_report_summary)
        btnReportSummary.setOnClickListener {
            val intent = android.content.Intent(this, ReportSummaryActivity::class.java)
            startActivity(intent)
        }
    }
}
