package com.simats.boneloss

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PredictionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prediction)

        val tvPrediction = findViewById<TextView>(R.id.tv_prediction)
        val tvConfidence = findViewById<TextView>(R.id.tv_confidence)

        val prediction = intent.getStringExtra("PREDICTION")
        val confidence = intent.getFloatExtra("CONFIDENCE", 0.0f)

        tvPrediction.text = "Prediction: $prediction"
        tvConfidence.text = "Confidence: $confidence"

        when (prediction) {
            "Mild" -> tvPrediction.setTextColor(Color.GREEN)
            "Moderate" -> tvPrediction.setTextColor(Color.parseColor("#FFA500")) // Orange
            "Severe" -> tvPrediction.setTextColor(Color.RED)
        }
    }
}
