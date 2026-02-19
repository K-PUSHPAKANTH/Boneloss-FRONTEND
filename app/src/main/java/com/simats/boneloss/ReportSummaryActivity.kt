package com.simats.boneloss

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
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
        findViewById<ImageView>(R.id.btn_download).setOnClickListener { exportPdf() }
        findViewById<Button>(R.id.btn_share_report).setOnClickListener { shareReport() }
        findViewById<Button>(R.id.btn_export_pdf).setOnClickListener { exportPdf() }
        findViewById<Button>(R.id.btn_done).setOnClickListener {
            android.widget.Toast.makeText(this, "Report saved to patient history", android.widget.Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PatientHistoryActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun populateHeader() {
        findViewById<android.widget.ProgressBar>(R.id.pb_bone_loss_gauge).progress = 32
        findViewById<android.widget.TextView>(R.id.tv_bone_loss_percent_main).text = "32.5%"
    }

    private fun populateFindings() {
        // Diagnosis Finding - Heart icon in screenshot
        val diagnosis = findViewById<android.view.View>(R.id.finding_diagnosis)
        diagnosis.findViewById<android.widget.TextView>(R.id.tv_finding_title).text = "Primary Diagnosis"
        diagnosis.findViewById<android.widget.TextView>(R.id.tv_finding_desc).text = "Moderate Chronic Periodontitis"
        diagnosis.findViewById<ImageView>(R.id.iv_finding_icon).setImageResource(R.drawable.ic_check_circle)

        // Teeth Affected Finding - Tooth/Report icon
        val teeth = findViewById<android.view.View>(R.id.finding_teeth)
        teeth.findViewById<android.widget.TextView>(R.id.tv_finding_title).text = "Affected Teeth"
        teeth.findViewById<android.widget.TextView>(R.id.tv_finding_desc).text = "11, 12, 14, 21, 24, 26"
        teeth.findViewById<ImageView>(R.id.iv_finding_icon).setImageResource(R.drawable.ic_report)

        // Risk Finding - Alert icon
        val risk = findViewById<android.view.View>(R.id.finding_risk)
        risk.findViewById<android.widget.TextView>(R.id.tv_finding_title).text = "Risk Assessment"
        risk.findViewById<android.widget.TextView>(R.id.tv_finding_desc).text = "High risk areas identified in Quad 1 & 3"
        risk.findViewById<ImageView>(R.id.iv_finding_icon).setImageResource(R.drawable.ic_report)
        risk.findViewById<ImageView>(R.id.iv_finding_icon).setColorFilter(android.graphics.Color.parseColor("#EF4444"))
    }

    private fun populateToothData() {
        bindToothItem(R.id.item_tooth_11, "11", "#11", "22%", 22)
        bindToothItem(R.id.item_tooth_12, "12", "#12", "18%", 18)
        bindToothItem(R.id.item_tooth_13, "13", "#13", "12%", 12)
        bindToothItem(R.id.item_tooth_14, "14", "#14", "48%", 48)
    }

    private fun bindToothItem(id: Int, circleNum: String, idText: String, lossPercent: String, progress: Int) {
        val view = findViewById<android.view.View>(id)
        view.findViewById<android.widget.TextView>(R.id.tv_tooth_circle).text = circleNum
        view.findViewById<android.widget.TextView>(R.id.tv_tooth_id_text).text = idText
        view.findViewById<android.widget.TextView>(R.id.tv_loss_percent_val).text = lossPercent
        view.findViewById<android.widget.ProgressBar>(R.id.pb_loss_indicator).progress = progress
        
        // Color coding percent
        val color = when {
            progress > 40 -> "#EF4444"
            progress > 25 -> "#F97316"
            progress > 15 -> "#EAB308"
            else -> "#22C55E"
        }
        view.findViewById<android.widget.TextView>(R.id.tv_loss_percent_val).setTextColor(android.graphics.Color.parseColor(color))
    }

    private fun populateTreatmentPlan() {
        val phase1 = findViewById<android.view.View>(R.id.phase_1)
        phase1.findViewById<android.widget.TextView>(R.id.tv_phase_title).text = "Phase I: Urgent Care"
        phase1.findViewById<android.widget.TextView>(R.id.tv_phase_desc).text = "Scaling and root planing for high risk teeth (14, 26). Antibiotoic therapy."
        
        val phase2 = findViewById<android.view.View>(R.id.phase_2)
        phase2.findViewById<android.widget.TextView>(R.id.tv_phase_title).text = "Phase II: Maintenance"
        phase2.findViewById<android.widget.TextView>(R.id.tv_phase_desc).text = "Oral hygiene instruction and 3-month follow-up evaluation."
    }

    private fun shareReport() {
        android.widget.Toast.makeText(this, "Sharing Report...", android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun exportPdf() {
        val intent = Intent(this, PdfExportActivity::class.java)
        startActivity(intent)
    }
}
