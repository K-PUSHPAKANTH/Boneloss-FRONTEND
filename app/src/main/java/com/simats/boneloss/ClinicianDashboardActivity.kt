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
    private lateinit var navHistory: LinearLayout
    private lateinit var navProfile: LinearLayout
    
    private lateinit var ivHome: ImageView
    private lateinit var tvHome: TextView
    private lateinit var ivAnalytics: ImageView
    private lateinit var tvAnalytics: TextView
    private lateinit var ivHistory: ImageView
    private lateinit var tvHistory: TextView
    private lateinit var ivProfile: ImageView
    private lateinit var tvProfile: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clinician_dashboard)

        initViews()
        setupNavigation()

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(HomeFragment(), "HOME")
            updateNavUI("HOME")
        }

        // FAB
        findViewById<FloatingActionButton>(R.id.fab_add).setOnClickListener {
            val intent = Intent(this, UploadRadiographActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initViews() {
        navHome = findViewById(R.id.nav_home)
        navAnalytics = findViewById(R.id.nav_analytics)
        navHistory = findViewById(R.id.nav_patients)
        navProfile = findViewById(R.id.nav_profile)

        ivHome = navHome.getChildAt(0) as ImageView
        tvHome = navHome.getChildAt(1) as TextView
        
        ivAnalytics = navAnalytics.getChildAt(0) as ImageView
        tvAnalytics = navAnalytics.getChildAt(1) as TextView
        
        ivHistory = navHistory.getChildAt(0) as ImageView
        tvHistory = navHistory.getChildAt(1) as TextView
        
        ivProfile = navProfile.getChildAt(0) as ImageView
        tvProfile = navProfile.getChildAt(1) as TextView
    }

    private fun setupNavigation() {
        navHome.setOnClickListener {
            loadFragment(HomeFragment(), "HOME")
            updateNavUI("HOME")
        }
        navAnalytics.setOnClickListener {
            loadFragment(AnalyticsFragment(), "ANALYTICS")
            updateNavUI("ANALYTICS")
        }
        navHistory.setOnClickListener {
            loadFragment(HistoryFragment(), "HISTORY")
            updateNavUI("HISTORY")
        }
        navProfile.setOnClickListener {
            loadFragment(ProfileFragment(), "PROFILE")
            updateNavUI("PROFILE")
        }
    }

    private fun loadFragment(fragment: Fragment, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, fragment, tag)
        transaction.commit()
    }

    private fun updateNavUI(tag: String) {
        val activeColor = getColor(R.color.primary_blue)
        val inactiveColor = getColor(R.color.text_gray)

        ivHome.setColorFilter(if (tag == "HOME") activeColor else inactiveColor)
        tvHome.setTextColor(if (tag == "HOME") activeColor else inactiveColor)

        ivAnalytics.setColorFilter(if (tag == "ANALYTICS") activeColor else inactiveColor)
        tvAnalytics.setTextColor(if (tag == "ANALYTICS") activeColor else inactiveColor)

        ivHistory.setColorFilter(if (tag == "HISTORY") activeColor else inactiveColor)
        tvHistory.setTextColor(if (tag == "HISTORY") activeColor else inactiveColor)

        ivProfile.setColorFilter(if (tag == "PROFILE") activeColor else inactiveColor)
        tvProfile.setTextColor(if (tag == "PROFILE") activeColor else inactiveColor)
    }

    fun navigateToAnalytics() {
        loadFragment(AnalyticsFragment(), "ANALYTICS")
        updateNavUI("ANALYTICS")
    }

    fun navigateToHistory() {
        loadFragment(HistoryFragment(), "HISTORY")
        updateNavUI("HISTORY")
    }
}
