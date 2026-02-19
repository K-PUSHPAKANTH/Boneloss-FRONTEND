package com.simats.boneloss

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

import android.content.Context.MODE_PRIVATE
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.simats.boneloss.databinding.FragmentProfileBinding

import android.util.Log

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        
        setupListeners()
        observeViewModel()
        
        // Retrieve user_id from SharedPreferences
        val sharedPrefs = requireContext().getSharedPreferences("user_profile", MODE_PRIVATE)
        val userId = sharedPrefs.getInt("user_id", -1)
        
        Log.d("ProfileFragment", "Retrieved userId: $userId")
        
        if (userId != -1) {
            viewModel.fetchProfile(userId)
        } else {
            Log.e("ProfileFragment", "UserId not found in SharedPreferences")
            Toast.makeText(context, "User session expired", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun setupListeners() {
        binding.btnEditProfileFrag.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnNotificationsFrag.setOnClickListener {
            val intent = Intent(requireContext(), NotificationsSettingsActivity::class.java)
            startActivity(intent)
        }

        binding.btnPrivacyFrag.setOnClickListener {
            val intent = Intent(requireContext(), PrivacySecurityActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogoutSectionFrag.setOnClickListener {
            // Clear session data if needed
            requireContext().getSharedPreferences("user_profile", MODE_PRIVATE).edit().clear().apply()
            
            val intent = Intent(requireContext(), RoleSelectionActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.pbProfileLoading.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        }

        viewModel.profileResponse.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!
                updateUI(user)
            } else {
                Toast.makeText(context, "Failed to load profile", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(user: User) {
        binding.tvUserNameFrag.text = user.fullName
        binding.tvUserEmailFrag.text = user.email
        binding.tvUserRoleFrag.text = user.role
        
        val initials = user.fullName.split(" ")
            .filter { it.isNotEmpty() }
            .take(2)
            .map { it[0] }
            .joinToString("")
            .uppercase()
            
        binding.tvAvatarInitialsFrag.text = initials
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
