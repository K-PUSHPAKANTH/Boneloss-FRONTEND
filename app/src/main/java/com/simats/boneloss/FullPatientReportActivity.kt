package com.simats.boneloss

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FullPatientReportActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var scrollView: View
    private lateinit var adapter: ToothAnalyticsAdapter
    private var currentReport: PatientReport? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_summary)

        val analysisId = intent.getIntExtra("ANALYSIS_ID", -1)
        if (analysisId == -1) {
            Toast.makeText(this, "Invalid Analysis ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        initViews()
        fetchReport(analysisId)
    }

    private fun initViews() {
        progressBar = findViewById(R.id.progress_bar)
        scrollView = findViewById(R.id.scroll_view)
        
        val rvAnalytics = findViewById<RecyclerView>(R.id.rv_tooth_analytics)
        adapter = ToothAnalyticsAdapter(emptyList())
        rvAnalytics.layoutManager = LinearLayoutManager(this)
        rvAnalytics.adapter = adapter

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }
        
        findViewById<Button>(R.id.btn_share_report).setOnClickListener { shareReport() }
        findViewById<ImageView>(R.id.btn_share_header).setOnClickListener { shareReport() }
        
        findViewById<Button>(R.id.btn_export_pdf).setOnClickListener { exportPdf() }
        findViewById<ImageView>(R.id.btn_download_header).setOnClickListener { exportPdf() }
        
        findViewById<Button>(R.id.btn_done).setOnClickListener {
            val sharedPrefs = getSharedPreferences("user_profile", MODE_PRIVATE)
            val userId = sharedPrefs.getInt("user_id", -1)
            
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putExtra("user_id", userId) 
            startActivity(intent)
            finish()
        }
    }

    private fun fetchReport(analysisId: Int) {
        progressBar.visibility = View.VISIBLE
        scrollView.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.getFullReport(analysisId)
                if (response.isSuccessful && response.body() != null) {
                    currentReport = response.body()
                    bindUi(currentReport!!)
                } else {
                    Toast.makeText(this@FullPatientReportActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@FullPatientReportActivity, "Connection failed: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
                scrollView.visibility = View.VISIBLE
            }
        }
    }

    private fun bindUi(report: PatientReport) {
        // Header
        findViewById<TextView>(R.id.tv_patient_id).text = report.patientId ?: "N/A"
        findViewById<TextView>(R.id.tv_report_date).text = report.reportDate ?: "N/A"
        findViewById<TextView>(R.id.tv_overall_status).text = report.overallStatus ?: "UNKNOWN"
        
        val badge = findViewById<TextView>(R.id.tv_severity_badge)
        val severity = report.severity ?: "unknown"
        badge.text = severity.uppercase()
        
        // Use standard colors first to ensure it compiles if themes are messed up
        when (severity.lowercase()) {
            "mild" -> {
                badge.setTextColor(Color.parseColor("#EAB308"))
                badge.setBackgroundResource(R.drawable.bg_priority_badge_yellow)
            }
            "moderate" -> {
                badge.setTextColor(Color.parseColor("#F97316"))
                badge.setBackgroundResource(R.drawable.bg_priority_badge)
            }
            "severe" -> {
                badge.setTextColor(Color.parseColor("#EF4444"))
                badge.setBackgroundResource(R.drawable.bg_priority_badge_red)
            }
            else -> {
                badge.setTextColor(Color.GREEN)
                badge.setBackgroundResource(R.drawable.bg_priority_badge_green)
            }
        }

        // Executive Summary
        val avgLoss = report.averageBoneLoss ?: 0.0
        findViewById<ProgressBar>(R.id.pb_bone_loss_gauge).progress = avgLoss.toInt()
        findViewById<TextView>(R.id.tv_bone_loss_percent_main).text = "${String.format("%.1f", avgLoss)}%"
        findViewById<TextView>(R.id.tv_executive_summary).text = report.executiveSummary ?: "Summary not available"

        // Key Findings
        val diagnosisView = findViewById<View>(R.id.finding_diagnosis)
        diagnosisView.findViewById<TextView>(R.id.tv_finding_title).text = "Primary Diagnosis"
        diagnosisView.findViewById<TextView>(R.id.tv_finding_desc).text = report.primaryDiagnosis ?: "Not diagnosed"
        diagnosisView.findViewById<ImageView>(R.id.iv_finding_icon).setImageResource(R.drawable.ic_check_circle)

        val teethView = findViewById<View>(R.id.finding_teeth)
        teethView.findViewById<TextView>(R.id.tv_finding_title).text = "Affected Teeth"
        teethView.findViewById<TextView>(R.id.tv_finding_desc).text = report.affectedTeeth?.joinToString(", ") ?: "None"
        teethView.findViewById<ImageView>(R.id.iv_finding_icon).setImageResource(R.drawable.ic_report)

        val riskView = findViewById<View>(R.id.finding_risk)
        riskView.findViewById<TextView>(R.id.tv_finding_title).text = "Risk Assessment"
        riskView.findViewById<TextView>(R.id.tv_finding_desc).text = report.riskAssessment ?: "Assessment not available"
        riskView.findViewById<ImageView>(R.id.iv_finding_icon).setImageResource(R.drawable.ic_report)
        riskView.findViewById<ImageView>(R.id.iv_finding_icon).setColorFilter(Color.RED)

        // Tooth Analytics List
        adapter.updateData(report.toothAnalytics ?: emptyList())
    }

    private fun shareReport() {
        val report = currentReport
        if (report == null) {
            Toast.makeText(this, "Report data not ready to share", Toast.LENGTH_SHORT).show()
            return
        }

        val shareText = """
            BONE LOSS ANALYSIS REPORT
            Patient ID: ${report.patientId ?: "N/A"}
            Date: ${report.reportDate ?: "N/A"}
            Status: ${report.overallStatus ?: "UNKNOWN"}
            Average Bone Loss: ${String.format("%.1f", report.averageBoneLoss ?: 0.0)}%
            Diagnosis: ${report.primaryDiagnosis ?: "N/A"}
            Affected Teeth: ${report.affectedTeeth?.joinToString(", ") ?: "None"}
            
            Summary: ${report.executiveSummary ?: "N/A"}
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Patient Report: ${report.patientId ?: "Analysis"}")
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(intent, "Share Report via"))
    }

    private fun exportPdf() {
        val analysisId = intent.getIntExtra("ANALYSIS_ID", -1)
        if (analysisId == -1) return

        progressBar.visibility = View.VISIBLE
        Toast.makeText(this, "Preparing PDF download...", Toast.LENGTH_SHORT).show()

        lifecycleScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            try {
                val response = ApiClient.apiService.exportPdf(analysisId)
                
                withContext(kotlinx.coroutines.Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val fileName = "BoneLoss_Report_${currentReport?.patientId ?: analysisId}.pdf"
                        val uri = FileHelper.savePdfToDownloads(this@FullPatientReportActivity, response.body()!!, fileName)
                        
                        progressBar.visibility = View.GONE
                        if (uri != null) {
                            Toast.makeText(this@FullPatientReportActivity, "PDF downloaded successfully", Toast.LENGTH_LONG).show()
                            FileHelper.openPdf(this@FullPatientReportActivity, uri)
                        } else {
                            Toast.makeText(this@FullPatientReportActivity, "Failed to save PDF", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@FullPatientReportActivity, "Export failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(kotlinx.coroutines.Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@FullPatientReportActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
