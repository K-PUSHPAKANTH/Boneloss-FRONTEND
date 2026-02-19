package com.simats.boneloss

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class NotificationsSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications_settings)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val switchPush = findViewById<Switch>(R.id.switch_push)
        val switchAnalysis = findViewById<Switch>(R.id.switch_analysis)
        val switchCase = findViewById<Switch>(R.id.switch_case)
        val switchWeekly = findViewById<Switch>(R.id.switch_weekly)
        val switchProduct = findViewById<Switch>(R.id.switch_product)
        val switchFollowup = findViewById<Switch>(R.id.switch_followup)

        val prefs = getSharedPreferences("notification_settings", Context.MODE_PRIVATE)

        // Load saved settings
        switchPush.isChecked = prefs.getBoolean("push_enabled", true)
        switchAnalysis.isChecked = prefs.getBoolean("analysis_enabled", true)
        switchCase.isChecked = prefs.getBoolean("case_enabled", true)
        switchWeekly.isChecked = prefs.getBoolean("weekly_enabled", false)
        switchProduct.isChecked = prefs.getBoolean("product_enabled", true)
        switchFollowup.isChecked = prefs.getBoolean("followup_enabled", true)

        btnBack.setOnClickListener {
            onBackPressed()
        }

        // Save settings on change
        switchPush.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("push_enabled", isChecked).apply()
        }
        switchAnalysis.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("analysis_enabled", isChecked).apply()
        }
        switchCase.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("case_enabled", isChecked).apply()
        }
        switchWeekly.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("weekly_enabled", isChecked).apply()
        }
        switchProduct.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("product_enabled", isChecked).apply()
        }
        switchFollowup.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("followup_enabled", isChecked).apply()
        }
    }
}
