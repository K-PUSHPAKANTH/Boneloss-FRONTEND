package com.simats.boneloss

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class AlveolarBoneLevelDetectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alveolar_bone_level)

        val imageUriString = intent.getStringExtra("IMAGE_URI")
        val imageUri = imageUriString?.let { Uri.parse(it) }

        val ivAnalyzedImage = findViewById<ImageView>(R.id.iv_analyzed_image)
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnFinish = findViewById<Button>(R.id.btn_finish)

        imageUri?.let {
            ivAnalyzedImage.setImageURI(it)
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnFinish.setOnClickListener {
            val intent = Intent(this, BoneLossMeasurementActivity::class.java)
            intent.putExtra("IMAGE_URI", imageUriString)
            startActivity(intent)
        }
    }
}
