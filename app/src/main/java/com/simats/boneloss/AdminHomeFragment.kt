package com.simats.boneloss

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

class AdminHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_home, container, false)

        val tvUserName = view.findViewById<TextView>(R.id.tv_user_name_home)
        val btnNotifications = view.findViewById<FrameLayout>(R.id.btn_notifications_home)
        val btnAnalytics = view.findViewById<LinearLayout>(R.id.btn_analytics_home)
        val btnSettings = view.findViewById<LinearLayout>(R.id.btn_settings_home)

        // Get user name from SharedPreferences
        val sharedPrefs = requireContext().getSharedPreferences("user_profile", android.content.Context.MODE_PRIVATE)
        val userName = sharedPrefs.getString("full_name", "Dr. Michael Chen")
        tvUserName.text = userName

        btnNotifications.setOnClickListener {
            startActivity(Intent(requireContext(), NotificationsActivity::class.java))
        }

        btnAnalytics.setOnClickListener {
            (activity as? ResearcherDashboardActivity)?.navigateToAnalytics()
        }

        btnSettings.setOnClickListener {
            (activity as? ResearcherDashboardActivity)?.navigateToProfile()
        }

        return view
    }
}
