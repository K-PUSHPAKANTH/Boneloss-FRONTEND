package com.simats.boneloss

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ClinicianDashboardActivity : AppCompatActivity() {

    private lateinit var navHome: LinearLayout
    private lateinit var navAnalytics: LinearLayout
    private lateinit var navFiles: LinearLayout
    private lateinit var navProfile: LinearLayout

    private lateinit var ivHome: ImageView
    private lateinit var tvHome: TextView
    private lateinit var ivAnalytics: ImageView
    private lateinit var tvAnalytics: TextView
    private lateinit var ivFiles: ImageView
    private lateinit var tvFiles: TextView
    private lateinit var ivProfile: ImageView
    private lateinit var tvProfile: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clinician_dashboard)

        initViews()
        setupNavigation()

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(DentistHomeFragment(), "HOME")
            updateNavUI("HOME")
        }

        // FAB
        findViewById<FloatingActionButton>(R.id.fab_add).setOnClickListener {
            val intent = Intent(this, UploadRadiographActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initViews() {
        navHome     = findViewById(R.id.nav_home)
        navAnalytics = findViewById(R.id.nav_analytics)
        navFiles    = findViewById(R.id.nav_patients)
        navProfile  = findViewById(R.id.nav_profile)

        // Use explicit IDs — safe, no index-based casting
        ivHome      = findViewById(R.id.iv_nav_home)
        tvHome      = findViewById(R.id.tv_nav_home)
        ivAnalytics = findViewById(R.id.iv_nav_analytics)
        tvAnalytics = findViewById(R.id.tv_nav_analytics)
        ivFiles     = findViewById(R.id.iv_nav_files)
        tvFiles     = findViewById(R.id.tv_nav_files)
        ivProfile   = findViewById(R.id.iv_nav_profile)
        tvProfile   = findViewById(R.id.tv_nav_profile)
    }

    private fun setupNavigation() {
        navHome.setOnClickListener {
            loadFragment(DentistHomeFragment(), "HOME")
            updateNavUI("HOME")
        }
        navAnalytics.setOnClickListener {
            loadFragment(DentistAnalyticsFragment(), "ANALYTICS")
            updateNavUI("ANALYTICS")
        }
        navFiles.setOnClickListener {
            loadFragment(DentistFilesFragment(), "FILES")
            updateNavUI("FILES")
        }
        navProfile.setOnClickListener {
            loadFragment(DentistProfileFragment(), "PROFILE")
            updateNavUI("PROFILE")
        }
    }

    private fun loadFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment, tag)
            .commit()
    }

    private fun updateNavUI(tag: String) {
        val activeColor   = getColor(R.color.primary_blue)
        val inactiveColor = getColor(R.color.text_gray)

        ivHome.setColorFilter(if (tag == "HOME") activeColor else inactiveColor)
        tvHome.setTextColor(if (tag == "HOME") activeColor else inactiveColor)

        ivAnalytics.setColorFilter(if (tag == "ANALYTICS") activeColor else inactiveColor)
        tvAnalytics.setTextColor(if (tag == "ANALYTICS") activeColor else inactiveColor)

        ivFiles.setColorFilter(if (tag == "FILES") activeColor else inactiveColor)
        tvFiles.setTextColor(if (tag == "FILES") activeColor else inactiveColor)

        ivProfile.setColorFilter(if (tag == "PROFILE") activeColor else inactiveColor)
        tvProfile.setTextColor(if (tag == "PROFILE") activeColor else inactiveColor)
    }

    fun navigateToAnalytics() {
        loadFragment(DentistAnalyticsFragment(), "ANALYTICS")
        updateNavUI("ANALYTICS")
    }

    fun navigateToFiles() {
        loadFragment(DentistFilesFragment(), "FILES")
        updateNavUI("FILES")
    }
}
