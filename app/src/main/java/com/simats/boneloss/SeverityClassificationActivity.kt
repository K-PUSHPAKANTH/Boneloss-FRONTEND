package com.simats.boneloss

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SeverityClassificationActivity : AppCompatActivity() {

    private lateinit var tvSeverityStatus: TextView
    private lateinit var tvAverageBoneLoss: TextView
    private lateinit var pbLoading: ProgressBar
    private lateinit var rvHeatmap: RecyclerView
    private lateinit var heatmapAdapter: HeatmapAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_severity_classification)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnToothBreakdown = findViewById<android.widget.Button>(R.id.btn_tooth_breakdown)
        
        tvSeverityStatus = findViewById(R.id.tv_severity_status)
        tvAverageBoneLoss = findViewById(R.id.tv_average_bone_loss)
        pbLoading = findViewById(R.id.pb_loading)
        rvHeatmap = findViewById(R.id.rv_heatmap)

        rvHeatmap.layoutManager = GridLayoutManager(this, 4) // 4 columns for teeth
        heatmapAdapter = HeatmapAdapter(emptyList())
        rvHeatmap.adapter = heatmapAdapter

        // Safely retrieve Intent data
        val prediction = intent.getStringExtra("prediction") ?: ""
        val confidence = intent.getDoubleExtra("confidence", 0.0)
        val averageBoneLossValue = intent.getDoubleExtra("average_bone_loss", 0.0)
        val heatmapJson = intent.getStringExtra("heatmap_json") ?: ""
        val analysisId = intent.getIntExtra("ANALYSIS_ID", -1)

        if (prediction.isNotEmpty()) {
            // Already have data from upload, display it directly
            updateUI(prediction, confidence.toFloat(), averageBoneLossValue.toFloat(), heatmapJson)
        } else if (analysisId != -1) {
            // No prediction passed, fetch via analysisId (e.g., from history)
            fetchAnalysisData(analysisId)
        } else {
            // Error case: no data passed
            tvSeverityStatus.text = "NO DATA AVAILABLE"
            tvAverageBoneLoss.text = "Average Bone Loss: N/A"
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnToothBreakdown.setOnClickListener {
            val intent = android.content.Intent(this, ClinicalInterpretationActivity::class.java)
            intent.putExtra("ANALYSIS_ID", analysisId)
            startActivity(intent)
        }
    }

    private fun updateUI(prediction: String, confidence: Float, averageBoneLoss: Float, heatmapJson: String) {
        updateSeverityHeader(prediction, confidence)
        tvAverageBoneLoss.text = "Average Bone Loss: $averageBoneLoss%"
        
        if (heatmapJson.isNotEmpty()) {
            try {
                val type = object : com.google.gson.reflect.TypeToken<List<HeatmapData>>() {}.type
                val heatmapData: List<HeatmapData> = com.google.gson.Gson().fromJson(heatmapJson, type)
                heatmapAdapter.updateData(heatmapData)
                rvHeatmap.visibility = View.VISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchAnalysisData(analysisId: Int) {
        pbLoading.visibility = View.VISIBLE
        rvHeatmap.visibility = View.GONE
        tvSeverityStatus.text = "ANALYSING IMAGE..."
        tvAverageBoneLoss.text = "Average Bone Loss: --%"

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.apiService.getAnalysis(analysisId)
                withContext(Dispatchers.Main) {
                    pbLoading.visibility = View.GONE
                    if (response.isSuccessful && response.body() != null) {
                        val body = response.body()!!
                        updateUI(
                            body.prediction ?: "Unknown",
                            body.confidence ?: 0f,
                            body.average_bone_loss ?: 0f,
                            com.google.gson.Gson().toJson(body.heatmap ?: emptyList<HeatmapData>())
                        )
                    } else {
                        Toast.makeText(this@SeverityClassificationActivity, "Failed to load heatmap data", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    pbLoading.visibility = View.GONE
                    Toast.makeText(this@SeverityClassificationActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateSeverityHeader(prediction: String, confidence: Float) {
        // Backend returns confidence in 0-100% already
        val confidencePercent = String.format("%.2f", confidence)
        tvSeverityStatus.text = "${prediction.uppercase()} - $confidencePercent% CONFIDENCE"

        when (prediction) {
            "Healthy" -> tvSeverityStatus.setTextColor(android.graphics.Color.parseColor("#10B981"))
            "Mild" -> tvSeverityStatus.setTextColor(android.graphics.Color.parseColor("#F59E0B"))
            "Moderate" -> tvSeverityStatus.setTextColor(android.graphics.Color.parseColor("#F97316"))
            "Severe" -> tvSeverityStatus.setTextColor(android.graphics.Color.parseColor("#EF4444"))
            else -> tvSeverityStatus.setTextColor(android.graphics.Color.WHITE)
        }
    }
}
