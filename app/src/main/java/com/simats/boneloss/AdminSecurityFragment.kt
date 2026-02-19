package com.simats.boneloss

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import android.content.Intent
import android.net.Uri
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class AdminSecurityFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_security, container, false)

        val btnBack = view.findViewById<ImageView>(R.id.btn_back_security)
        val btnPrivacyPolicy = view.findViewById<Button>(R.id.btn_view_privacy_policy)

        btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        btnPrivacyPolicy.setOnClickListener {
            // In a real app, this would open a URL. 
            // For now, we'll show a toast or a placeholder URL.
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.simats.edu/privacy-policy"))
            try {
                startActivity(browserIntent)
            } catch (e: Exception) {
                Toast.makeText(context, "Privacy Policy link not available", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
