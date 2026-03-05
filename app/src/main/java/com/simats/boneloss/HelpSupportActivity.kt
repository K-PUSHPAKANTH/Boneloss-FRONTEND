package com.simats.boneloss

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.simats.boneloss.databinding.ActivityHelpSupportBinding

class HelpSupportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelpSupportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnVideoTutorials.setOnClickListener {
            Toast.makeText(this, "Video tutorials coming soon", Toast.LENGTH_SHORT).show()
        }

        binding.btnContactSupport.setOnClickListener {
            Toast.makeText(this, "Opening contact support channel...", Toast.LENGTH_SHORT).show()
        }
    }
}
