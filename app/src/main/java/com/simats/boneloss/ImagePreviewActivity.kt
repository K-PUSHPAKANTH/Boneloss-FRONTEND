package com.simats.boneloss

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ImagePreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)

        val imageUriString = intent.getStringExtra("IMAGE_URI")
        val imageUri = imageUriString?.let { Uri.parse(it) }

        val ivPreview = findViewById<ImageView>(R.id.iv_preview)
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnConfirm = findViewById<Button>(R.id.btn_confirm)
        val btnReupload = findViewById<Button>(R.id.btn_reupload)

        imageUri?.let {
            ivPreview.setImageURI(it)
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnReupload.setOnClickListener {
            finish() // Go back to the upload screen
        }

        btnConfirm.setOnClickListener {
            // Navigate to AnalysisLoadingActivity
            val intent = Intent(this, AnalysisLoadingActivity::class.java)
            // Pass the URI to the loading activity
            intent.putExtra("IMAGE_URI", imageUriString)
            startActivity(intent)
        }
    }
}
