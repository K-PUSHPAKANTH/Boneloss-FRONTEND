package com.simats.boneloss

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import com.simats.boneloss.databinding.FragmentAdminProfileBinding

class AdminProfileFragment : Fragment() {
    private var _binding: FragmentAdminProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminProfileBinding.inflate(inflater, container, false)
        
        setupListeners()
        observeViewModel()
        
        val sharedPrefs = requireContext().getSharedPreferences("user_profile", Context.MODE_PRIVATE)
        val userId = sharedPrefs.getInt("user_id", -1)
        
        if (userId != -1) {
            viewModel.fetchProfile(userId)
        } else {
            Toast.makeText(context, "User session expired", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun setupListeners() {
        binding.btnBackProfileAdmin.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnPrivacySecurityAdmin.setOnClickListener {
            (activity as? ResearcherDashboardActivity)?.navigateToSecurity()
        }

        binding.btnLogoutAdmin.setOnClickListener {
            requireContext().getSharedPreferences("user_profile", Context.MODE_PRIVATE).edit().clear().apply()
            val intent = Intent(requireContext(), RoleSelectionActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }
        
        binding.btnEditProfileAdmin.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.pbProfileLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.profileResponse.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!
                updateUI(user)
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(user: User) {
        binding.tvAdminName.text = user.fullName
        binding.tvAdminEmail.text = user.email
        binding.tvUserRoleAdmin.text = user.role
        
        val initials = user.fullName.split(" ")
            .filter { it.isNotEmpty() }
            .take(2)
            .map { it[0].uppercaseChar() }
            .joinToString("")
            
        binding.tvAvatarInitialsAdmin.text = initials
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
