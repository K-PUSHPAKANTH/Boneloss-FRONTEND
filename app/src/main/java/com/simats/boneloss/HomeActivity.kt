package com.simats.boneloss

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val tvUserName = findViewById<TextView>(R.id.tv_user_name)
        val btnUploadXray = findViewById<LinearLayout>(R.id.btn_upload_xray)
        val btnScanImage = findViewById<LinearLayout>(R.id.btn_scan_image)
        val btnViewAll = findViewById<TextView>(R.id.btn_view_all)

        val navHome = findViewById<LinearLayout>(R.id.nav_home)
        val navAnalytics = findViewById<LinearLayout>(R.id.nav_analytics_bottom)
        val navFab = findViewById<ImageView>(R.id.nav_fab)
        val navFiles = findViewById<LinearLayout>(R.id.nav_files)
        val navProfile = findViewById<LinearLayout>(R.id.nav_profile)

        // Get user name from intent or SharedPreferences if passed
        val sharedPrefs = getSharedPreferences("user_profile", android.content.Context.MODE_PRIVATE)
        val savedName = sharedPrefs.getString("full_name", "Dr. Sarah Johnson")
        val userName = intent.getStringExtra("user_name") ?: savedName
        tvUserName.text = userName

        // Notification Bell
        val btnNotification = findViewById<FrameLayout>(R.id.btn_notification)
        btnNotification.setOnClickListener {
            val intent = Intent(this, NotificationsSettingsActivity::class.java)
            startActivity(intent)
        }

        // Action Buttons
        btnUploadXray.setOnClickListener {
            val intent = Intent(this, UploadRadiographActivity::class.java)
            startActivity(intent)
        }

        btnScanImage.setOnClickListener {
            // Placeholder for scan functionality
            val intent = Intent(this, UploadRadiographActivity::class.java)
            startActivity(intent)
        }

        btnViewAll.setOnClickListener {
            val intent = Intent(this, PatientHistoryActivity::class.java)
            startActivity(intent)
        }

        // Bottom Navigation
        navAnalytics.setOnClickListener {
            val intent = Intent(this, AnalyticsActivity::class.java)
            startActivity(intent)
        }

        navProfile.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        navFiles.setOnClickListener {
            val intent = Intent(this, PatientHistoryActivity::class.java)
            startActivity(intent)
        }

        navFab.setOnClickListener {
            val intent = Intent(this, UploadRadiographActivity::class.java)
            startActivity(intent)
        }
        
        navHome.setOnClickListener {
            // Already on Home
        }
    }
}
