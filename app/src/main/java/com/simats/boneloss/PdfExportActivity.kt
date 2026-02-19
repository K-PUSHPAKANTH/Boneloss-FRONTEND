package com.simats.boneloss

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PdfExportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_export)

        setupActions()
    }

    private fun setupActions() {
        findViewById<ImageView>(R.id.btn_close).setOnClickListener {
            finish()
        }

        findViewById<ImageView>(R.id.btn_share_top).setOnClickListener {
            sharePdf()
        }

        findViewById<Button>(R.id.btn_share_bottom).setOnClickListener {
            sharePdf()
        }

        findViewById<Button>(R.id.btn_save_pdf).setOnClickListener {
            savePdf()
        }
    }

    private fun sharePdf() {
        Toast.makeText(this, "Opening share sheet for PDF...", Toast.LENGTH_SHORT).show()
    }

    private fun savePdf() {
        Toast.makeText(this, "Saving PDF to Downloads folder...", Toast.LENGTH_LONG).show()
        // Simulate a delay for "saving"
        findViewById<Button>(R.id.btn_save_pdf).postDelayed({
            Toast.makeText(this, "Report saved successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }, 1500)
    }
}
