package com.simats.boneloss

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment

class StudentSavedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_saved, container, false)
        
        view.findViewById<ImageView>(R.id.btn_back_saved).setOnClickListener {
            // Since this is a dashboard fragment, "back" could navigate to Home
            (activity as? StudentDashboardActivity)?.navigateToHome()
        }
        
        view.findViewById<Button>(R.id.btn_load_more_saved).setOnClickListener {
            // Handle load more
        }
        
        return view
    }
}
