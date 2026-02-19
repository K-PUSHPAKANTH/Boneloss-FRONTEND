package com.simats.boneloss

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class BoneLossMeasurementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bone_loss_measurement)

        val imageUriString = intent.getStringExtra("IMAGE_URI")
        val imageUri = imageUriString?.let { Uri.parse(it) }

        val ivVisual = findViewById<ImageView>(R.id.iv_visual)
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnViewAnalysis = findViewById<android.widget.Button>(R.id.btn_view_analysis)

        imageUri?.let {
            ivVisual.setImageURI(it)
        }

        setupOverallAssessment()
        setupToothMeasurements()

        btnBack.setOnClickListener {
            finish()
        }

        btnViewAnalysis.setOnClickListener {
            val intent = Intent(this, DistributionAnalysisActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupOverallAssessment() {
        findViewById<android.widget.TextView>(R.id.tv_overall_percent).text = "32.5%"
        findViewById<android.widget.TextView>(R.id.tv_overall_status).text = "Moderate Periodontitis"
        findViewById<android.widget.ProgressBar>(R.id.pb_overall_loss).progress = 32
    }

    private fun setupToothMeasurements() {
        bindToothData(R.id.item_tooth_11, "11", "#11", "12.5mm", "22%", "2.8mm", 22)
        bindToothData(R.id.item_tooth_12, "12", "#12", "13.0mm", "25%", "3.2mm", 25)
        bindToothData(R.id.item_tooth_13, "13", "#13", "11.8mm", "18%", "2.1mm", 18)
        bindToothData(R.id.item_tooth_14, "14", "#14", "12.2mm", "15%", "1.8mm", 15)
        bindToothData(R.id.item_tooth_15, "15", "#15", "12.0mm", "20%", "2.4mm", 20)
    }

    private fun bindToothData(
        layoutId: Int,
        circleText: String,
        idText: String,
        rootVal: String,
        percentVal: String,
        mmVal: String,
        progress: Int
    ) {
        val view = findViewById<android.view.View>(layoutId)
        view.findViewById<android.widget.TextView>(R.id.tv_tooth_circle).text = circleText
        view.findViewById<android.widget.TextView>(R.id.tv_tooth_id_text).text = idText
        view.findViewById<android.widget.TextView>(R.id.tv_root_detail).text = rootVal
        view.findViewById<android.widget.TextView>(R.id.tv_loss_percent_val).text = percentVal
        view.findViewById<android.widget.TextView>(R.id.tv_loss_mm_val).text = mmVal
        view.findViewById<android.widget.ProgressBar>(R.id.pb_loss_indicator).progress = progress
    }
}
