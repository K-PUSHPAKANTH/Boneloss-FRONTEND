package com.simats.boneloss

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StudentDashboardActivity : AppCompatActivity() {

    private lateinit var navHome: LinearLayout
    private lateinit var navLearn: LinearLayout
    private lateinit var navSaved: LinearLayout
    private lateinit var navProfile: LinearLayout
    
    private lateinit var ivHome: ImageView
    private lateinit var tvHome: TextView
    private lateinit var ivLearn: ImageView
    private lateinit var tvLearn: TextView
    private lateinit var ivSaved: ImageView
    private lateinit var tvSaved: TextView
    private lateinit var ivProfile: ImageView
    private lateinit var tvProfile: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_dashboard)

        initViews()
        setupNavigation()

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(StudentHomeFragment(), "HOME")
            updateNavUI("HOME")
        }

        findViewById<ImageView>(R.id.nav_fab).setOnClickListener {
            val intent = Intent(this, UploadRadiographActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initViews() {
        navHome = findViewById(R.id.nav_home)
        navLearn = findViewById(R.id.nav_learn)
        navSaved = findViewById(R.id.nav_saved)
        navProfile = findViewById(R.id.nav_profile)

        ivHome = navHome.getChildAt(0) as ImageView
        tvHome = navHome.getChildAt(1) as TextView
        
        ivLearn = navLearn.getChildAt(0) as ImageView
        tvLearn = navLearn.getChildAt(1) as TextView
        
        ivSaved = navSaved.getChildAt(0) as ImageView
        tvSaved = navSaved.getChildAt(1) as TextView
        
        ivProfile = navProfile.getChildAt(0) as ImageView
        tvProfile = navProfile.getChildAt(1) as TextView
    }

    private fun setupNavigation() {
        navHome.setOnClickListener {
            loadFragment(StudentHomeFragment(), "HOME")
            updateNavUI("HOME")
        }
        navLearn.setOnClickListener {
            loadFragment(StudentLearnFragment(), "LEARN")
            updateNavUI("LEARN")
        }
        navSaved.setOnClickListener {
            loadFragment(StudentSavedFragment(), "SAVED")
            updateNavUI("SAVED")
        }
        navProfile.setOnClickListener {
            loadFragment(StudentProfileFragment(), "PROFILE")
            updateNavUI("PROFILE")
        }
    }

    private fun loadFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.student_nav_host_fragment, fragment, tag)
        transaction.commit()
    }

    private fun updateNavUI(tag: String) {
        val activeColor = getColor(R.color.primary_blue)
        val inactiveColor = getColor(R.color.text_gray)

        ivHome.setColorFilter(if (tag == "HOME") activeColor else inactiveColor)
        tvHome.setTextColor(if (tag == "HOME") activeColor else inactiveColor)

        ivLearn.setColorFilter(if (tag == "LEARN") activeColor else inactiveColor)
        tvLearn.setTextColor(if (tag == "LEARN") activeColor else inactiveColor)

        ivSaved.setColorFilter(if (tag == "SAVED") activeColor else inactiveColor)
        tvSaved.setTextColor(if (tag == "SAVED") activeColor else inactiveColor)

        ivProfile.setColorFilter(if (tag == "PROFILE") activeColor else inactiveColor)
        tvProfile.setTextColor(if (tag == "PROFILE") activeColor else inactiveColor)
    }

    fun navigateToLearn() {
        loadFragment(StudentLearnFragment(), "LEARN")
        updateNavUI("LEARN")
    }

    fun navigateToSaved() {
        loadFragment(StudentSavedFragment(), "SAVED")
        updateNavUI("SAVED")
    }

    fun navigateToHome() {
        loadFragment(StudentHomeFragment(), "HOME")
        updateNavUI("HOME")
    }
}


