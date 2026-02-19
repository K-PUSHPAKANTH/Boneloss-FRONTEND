package com.simats.boneloss

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ToothDetectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tooth_detection)

        val imageUriString = intent.getStringExtra("IMAGE_URI")
        val imageUri = imageUriString?.let { Uri.parse(it) }

        val ivAnalyzedImage = findViewById<ImageView>(R.id.iv_analyzed_image)
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnDone = findViewById<Button>(R.id.btn_done)

        imageUri?.let {
            ivAnalyzedImage.setImageURI(it)
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnDone.setOnClickListener {
            val intent = Intent(this, AlveolarBoneLevelDetectionActivity::class.java)
            intent.putExtra("IMAGE_URI", imageUriString)
            startActivity(intent)
        }
    }
}
