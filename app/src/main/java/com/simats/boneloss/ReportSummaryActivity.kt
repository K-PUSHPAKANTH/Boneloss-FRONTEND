package com.simats.boneloss

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ReportSummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_summary)

        setupNavigation()
        populateHeader()
        populateFindings()
        populateToothData()
        populateTreatmentPlan()
    }

    private fun setupNavigation() {
        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }
        findViewById<ImageView>(R.id.btn_share_header).setOnClickListener { shareReport() }
        findViewById<ImageView>(R.id.btn_download_header).setOnClickListener { exportPdf() }
        findViewById<Button>(R.id.btn_share_report).setOnClickListener { shareReport() }
        findViewById<Button>(R.id.btn_export_pdf).setOnClickListener { exportPdf() }
        findViewById<Button>(R.id.btn_done).setOnClickListener {
            val sharedPrefs = getSharedPreferences("user_profile", MODE_PRIVATE)
            val userId = sharedPrefs.getInt("user_id", -1)
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putExtra("user_id", userId)
            startActivity(intent)
            finish()
        }
    }

    private fun populateHeader() {
        findViewById<android.widget.ProgressBar>(R.id.pb_bone_loss_gauge).progress = 32
        findViewById<android.widget.TextView>(R.id.tv_bone_loss_percent_main).text = "32.5%"
    }

    private fun populateFindings() {
        val diagnosis = findViewById<android.view.View>(R.id.finding_diagnosis)
        diagnosis.findViewById<android.widget.TextView>(R.id.tv_finding_title).text = "Primary Diagnosis"
        diagnosis.findViewById<android.widget.TextView>(R.id.tv_finding_desc).text = "Moderate Chronic Periodontitis"
        diagnosis.findViewById<ImageView>(R.id.iv_finding_icon).setImageResource(R.drawable.ic_check_circle)

        val teeth = findViewById<android.view.View>(R.id.finding_teeth)
        teeth.findViewById<android.widget.TextView>(R.id.tv_finding_title).text = "Affected Teeth"
        teeth.findViewById<android.widget.TextView>(R.id.tv_finding_desc).text = "11, 12, 14, 21, 24, 26"
        teeth.findViewById<ImageView>(R.id.iv_finding_icon).setImageResource(R.drawable.ic_report)

        val risk = findViewById<android.view.View>(R.id.finding_risk)
        risk.findViewById<android.widget.TextView>(R.id.tv_finding_title).text = "Risk Assessment"
        risk.findViewById<android.widget.TextView>(R.id.tv_finding_desc).text = "High risk areas identified in Quad 1 & 3"
        risk.findViewById<ImageView>(R.id.iv_finding_icon).setImageResource(R.drawable.ic_report)
        risk.findViewById<ImageView>(R.id.iv_finding_icon).setColorFilter(android.graphics.Color.parseColor("#EF4444"))
    }

    private fun populateToothData() {
        // This is a simplified version for populating some data
        // In reality, this would be dynamic from the API
    }

    private fun populateTreatmentPlan() {
        val phase1 = findViewById<android.view.View>(R.id.phase_1)
        if (phase1 != null) {
            phase1.findViewById<android.widget.TextView>(R.id.tv_phase_title).text = "Phase I: Urgent Care"
            phase1.findViewById<android.widget.TextView>(R.id.tv_phase_desc).text = "Scaling and root planing for high risk teeth (14, 26). Antibiotic therapy."
        }
        
        val phase2 = findViewById<android.view.View>(R.id.phase_2)
        if (phase2 != null) {
            phase2.findViewById<android.widget.TextView>(R.id.tv_phase_title).text = "Phase II: Maintenance"
            phase2.findViewById<android.widget.TextView>(R.id.tv_phase_desc).text = "Oral hygiene instruction and 3-month follow-up evaluation."
        }
    }

    private fun shareReport() {
        Toast.makeText(this, "Sharing Report...", Toast.LENGTH_SHORT).show()
    }

    private fun exportPdf() {
        // Navigation to export logic or call FileHelper
        Toast.makeText(this, "Exporting PDF...", Toast.LENGTH_SHORT).show()
    }
}
