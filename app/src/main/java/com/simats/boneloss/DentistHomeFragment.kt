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

class DentistHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dentist_home, container, false)

        val toUpload = {
            val intent = Intent(requireContext(), UploadRadiographActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<LinearLayout>(R.id.btn_upload_xray_dentist).setOnClickListener { toUpload() }
        view.findViewById<LinearLayout>(R.id.btn_scan_image_dentist).setOnClickListener { toUpload() }

        view.findViewById<FrameLayout>(R.id.btn_notifications_dentist).setOnClickListener {
            val intent = Intent(requireContext(), NotificationsActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<LinearLayout>(R.id.card_total_cases_dentist).setOnClickListener {
            (activity as? ClinicianDashboardActivity)?.navigateToAnalytics()
        }

        view.findViewById<TextView>(R.id.tv_view_all_dentist).setOnClickListener {
            (activity as? ClinicianDashboardActivity)?.navigateToFiles()
        }

        // Set clinician name from SharedPreferences or fallback
        val sharedPrefs = requireContext().getSharedPreferences("user_profile", Context.MODE_PRIVATE)
        val userName = sharedPrefs.getString("full_name", "Dr. Sarah Johnson") ?: "Dr. Sarah Johnson"
        view.findViewById<TextView>(R.id.tv_dentist_name).text = userName

        return view
    }
}
