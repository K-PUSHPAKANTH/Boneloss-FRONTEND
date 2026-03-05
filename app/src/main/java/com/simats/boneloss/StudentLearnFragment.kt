package com.simats.boneloss

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class StudentLearnFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_learn, container, false)
        
        val btnContinue = view.findViewById<android.widget.Button>(R.id.btn_continue_radiographic_fragment)
        btnContinue?.setOnClickListener {
            val intent = Intent(requireContext(), RadiographicInterpretationActivity::class.java)
            startActivity(intent)
        }
        
        return view
    }
}
