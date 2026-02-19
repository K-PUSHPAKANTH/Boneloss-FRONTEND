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

class StudentHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_home, container, false)

        val toUpload = {
            val intent = Intent(requireContext(), UploadRadiographActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<LinearLayout>(R.id.btn_upload_xray_home).setOnClickListener { toUpload() }
        view.findViewById<LinearLayout>(R.id.btn_scan_image_home).setOnClickListener { toUpload() }
        
        view.findViewById<FrameLayout>(R.id.btn_notification_home).setOnClickListener {
            val intent = Intent(requireContext(), NotificationsSettingsActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<TextView>(R.id.tv_view_all_modules_home).setOnClickListener {
            (activity as? StudentDashboardActivity)?.navigateToLearn()
        }
        
        view.findViewById<TextView>(R.id.tv_view_all_cases_home).setOnClickListener {
            (activity as? StudentDashboardActivity)?.navigateToSaved()
        }

        // Set student name
        view.findViewById<TextView>(R.id.tv_user_name_home).text = "Dr. Michael Chen"

        return view
    }
}
