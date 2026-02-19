package com.simats.boneloss

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val toUpload = {
            val intent = Intent(requireContext(), UploadRadiographActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<LinearLayout>(R.id.btn_upload_xray_home).setOnClickListener { toUpload() }
        view.findViewById<LinearLayout>(R.id.btn_scan_image_home).setOnClickListener { toUpload() }
        
        view.findViewById<FrameLayout>(R.id.btn_notifications_home).setOnClickListener {
            val intent = Intent(requireContext(), NotificationsActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<LinearLayout>(R.id.card_total_cases).setOnClickListener {
            (activity as? ClinicianDashboardActivity)?.navigateToAnalytics()
        }
        view.findViewById<TextView>(R.id.tv_view_all_home).setOnClickListener {
            (activity as? ClinicianDashboardActivity)?.navigateToHistory()
        }

        // Set clinician name
        view.findViewById<TextView>(R.id.tv_clinician_name).text = "Dr. Sarah Johnson"

        return view
    }
}
