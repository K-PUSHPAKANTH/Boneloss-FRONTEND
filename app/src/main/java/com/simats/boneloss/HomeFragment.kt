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
            (activity as? ClinicianDashboardActivity)?.navigateToFiles()
        }

        // Set clinician name
        view.findViewById<TextView>(R.id.tv_clinician_name).text = "Dr. Sarah Johnson"

        updateLatestCase(view)

        return view
    }

    private fun updateLatestCase(view: View) {
        val sharedPref = requireActivity().getSharedPreferences("BoneLossPrefs", Context.MODE_PRIVATE)
        val latestPrediction = sharedPref.getString("LATEST_PREDICTION", null)
        val latestConfidence = sharedPref.getFloat("LATEST_CONFIDENCE", 0f)
        val latestAverageBoneLoss = sharedPref.getFloat("LATEST_AVERAGE_BONE_LOSS", 0f)

        if (latestPrediction != null) {
            val tvPatientStatus = view.findViewById<TextView>(R.id.tv_view_all_home).rootView.findViewWithTag<TextView>("latest_patient_status") ?: return
            
            val confidencePercent = String.format("%.2f", latestConfidence)
            val boneLossPercent = String.format("%.2f", latestAverageBoneLoss)
            
            tvPatientStatus.text = "$latestPrediction • $boneLossPercent% bone loss • $confidencePercent% confidence"
            
            when (latestPrediction) {
                "Healthy", "Mild" -> tvPatientStatus.setTextColor(android.graphics.Color.parseColor("#10B981"))
                "Moderate" -> tvPatientStatus.setTextColor(android.graphics.Color.parseColor("#F97316"))
                "Severe" -> tvPatientStatus.setTextColor(android.graphics.Color.parseColor("#EF4444"))
                else -> tvPatientStatus.setTextColor(android.graphics.Color.GRAY)
            }
        }
    }
}
