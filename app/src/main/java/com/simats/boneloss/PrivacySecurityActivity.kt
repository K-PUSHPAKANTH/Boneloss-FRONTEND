package com.simats.boneloss

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PrivacySecurityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_security)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val switchBiometric = findViewById<Switch>(R.id.switch_biometric)
        val switch2fa = findViewById<Switch>(R.id.switch_2fa)
        val switchAnalytics = findViewById<Switch>(R.id.switch_analytics)
        val switchLocal = findViewById<Switch>(R.id.switch_local)
        val btnChangePassword = findViewById<LinearLayout>(R.id.btn_change_password)
        val btnDownloadData = findViewById<LinearLayout>(R.id.btn_download_data)
        val btnDeleteAccount = findViewById<LinearLayout>(R.id.btn_delete_account)

        // Bottom Navigation
        val navHome = findViewById<LinearLayout>(R.id.nav_home)
        val navAnalytics = findViewById<LinearLayout>(R.id.nav_analytics)
        val navFab = findViewById<FrameLayout>(R.id.nav_fab)
        val navSecurity = findViewById<LinearLayout>(R.id.nav_security)
        val navProfile = findViewById<LinearLayout>(R.id.nav_profile)

        val prefs = getSharedPreferences("privacy_settings", Context.MODE_PRIVATE)

        // Load saved settings
        switchBiometric.isChecked = prefs.getBoolean("biometric_enabled", false)
        switch2fa.isChecked = prefs.getBoolean("2fa_enabled", false)
        switchAnalytics.isChecked = prefs.getBoolean("analytics_enabled", true)
        switchLocal.isChecked = prefs.getBoolean("local_storage", false)

        btnBack.setOnClickListener {
            onBackPressed()
        }

        // Save settings on change
        switchBiometric.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("biometric_enabled", isChecked).apply()
            Toast.makeText(this, if (isChecked) "Biometric login enabled" else "Biometric login disabled", Toast.LENGTH_SHORT).show()
        }
        switch2fa.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("2fa_enabled", isChecked).apply()
            Toast.makeText(this, if (isChecked) "Two-factor authentication enabled" else "Two-factor authentication disabled", Toast.LENGTH_SHORT).show()
        }
        switchAnalytics.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("analytics_enabled", isChecked).apply()
        }
        switchLocal.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("local_storage", isChecked).apply()
        }

        btnChangePassword.setOnClickListener {
            Toast.makeText(this, "Change password feature coming soon", Toast.LENGTH_SHORT).show()
        }

        btnDownloadData.setOnClickListener {
            Toast.makeText(this, "Preparing your data download...", Toast.LENGTH_SHORT).show()
        }

        btnDeleteAccount.setOnClickListener {
            Toast.makeText(this, "Please contact support to delete your account", Toast.LENGTH_SHORT).show()
        }

        // Bottom Navigation Listeners
        navHome.setOnClickListener {
            val intent = Intent(this, ResearcherDashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        navAnalytics.setOnClickListener {
            val intent = Intent(this, AnalyticsActivity::class.java)
            startActivity(intent)
        }

        navFab.setOnClickListener {
            // Center button (Records/Datasets)
            val intent = Intent(this, PatientHistoryActivity::class.java)
            startActivity(intent)
        }
        
        navSecurity.setOnClickListener {
            // Already on Privacy/Security
        }

        navProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
