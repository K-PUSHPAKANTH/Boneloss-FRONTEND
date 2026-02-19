package com.simats.boneloss

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class ResearcherDashboardActivity : AppCompatActivity() {

    private lateinit var navHome: LinearLayout
    private lateinit var navAnalytics: LinearLayout
    private lateinit var navSecurity: LinearLayout
    private lateinit var navProfile: LinearLayout
    private lateinit var navFab: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_researcher_dashboard)

        initViews()
        setupNavigation()

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(AdminHomeFragment())
            updateNavUI(navHome)
        }
    }

    private fun initViews() {
        navHome = findViewById(R.id.nav_home)
        navAnalytics = findViewById(R.id.nav_analytics)
        navSecurity = findViewById(R.id.nav_security)
        navProfile = findViewById(R.id.nav_profile)
        navFab = findViewById(R.id.nav_fab)
    }

    private fun setupNavigation() {
        navHome.setOnClickListener {
            loadFragment(AdminHomeFragment())
            updateNavUI(navHome)
        }

        navAnalytics.setOnClickListener {
            loadFragment(AdminAnalyticsFragment())
            updateNavUI(navAnalytics)
        }

        navSecurity.setOnClickListener {
            loadFragment(AdminSecurityFragment())
            updateNavUI(navSecurity)
        }

        navProfile.setOnClickListener {
            loadFragment(AdminProfileFragment())
            updateNavUI(navProfile)
        }

        navFab.setOnClickListener {
            // Center button (Records/Datasets) - Keep as separate activity for now or migrate to fragment?
            // Existing logic: Patients History
            startActivity(Intent(this, PatientHistoryActivity::class.java))
        }
    }

    fun navigateToHome() {
        loadFragment(AdminHomeFragment())
        updateNavUI(navHome)
    }

    fun navigateToAnalytics() {
        loadFragment(AdminAnalyticsFragment())
        updateNavUI(navAnalytics)
    }

    fun navigateToProfile() {
        loadFragment(AdminProfileFragment())
        updateNavUI(navProfile)
    }

    fun navigateToSecurity() {
        loadFragment(AdminSecurityFragment())
        updateNavUI(navSecurity)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.admin_fragment_container, fragment)
            .commit()
    }

    private fun updateNavUI(selectedNav: LinearLayout) {
        val navItems = listOf(navHome, navAnalytics, navSecurity, navProfile)
        
        for (item in navItems) {
            val icon = item.getChildAt(0)
            val text = item.getChildAt(if (item == navHome) 1 else 1) as? TextView
            
            // Special handling for Home which has a FrameLayout wrapper in original layout
            val actualIcon = if (icon is FrameLayout) icon.getChildAt(0) as ImageView else icon as ImageView

            if (item == selectedNav) {
                actualIcon.setColorFilter(Color.parseColor("#8B5CF6"))
                text?.let {
                    it.setTextColor(Color.parseColor("#8B5CF6"))
                    it.visibility = android.view.View.VISIBLE
                }
            } else {
                actualIcon.setColorFilter(Color.parseColor("#9CA3AF"))
                text?.setTextColor(Color.parseColor("#9CA3AF"))
            }
        }
    }
}
