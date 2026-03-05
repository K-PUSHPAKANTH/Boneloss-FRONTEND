package com.simats.boneloss

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.simats.boneloss.databinding.FragmentStudentHomeBinding

class StudentHomeFragment : Fragment() {

    private var _binding: FragmentStudentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var modulesAdapter: LearningModulesAdapter
    private lateinit var casesAdapter: SavedCasesAdapter

    // Integrated HomeViewModel with HomeRepository
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(HomeRepository(ApiClient.apiService))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerViews()
        setupListeners()
        setupObservers()
        
        loadData()
    }

    private fun setupRecyclerViews() {
        modulesAdapter = LearningModulesAdapter(emptyList())
        binding.rvLearningModules.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = modulesAdapter
        }

        casesAdapter = SavedCasesAdapter(emptyList())
        binding.rvSavedCases.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = casesAdapter
        }
    }

    private fun setupListeners() {
        val toUpload = { mode: String ->
            val intent = Intent(requireContext(), UploadRadiographActivity::class.java)
            intent.putExtra("UPLOAD_MODE", mode)
            startActivity(intent)
        }

        binding.btnUploadXrayHome.setOnClickListener { toUpload("UPLOAD") }
        binding.btnScanImageHome.setOnClickListener { toUpload("SCAN") }
        
        binding.btnNotificationHome.setOnClickListener {
            val intent = Intent(requireContext(), NotificationsSettingsActivity::class.java)
            startActivity(intent)
        }

        binding.tvViewAllModulesHome.setOnClickListener {
            (activity as? StudentDashboardActivity)?.navigateToLearn()
        }
        
        binding.tvViewAllCasesHome.setOnClickListener {
            (activity as? StudentDashboardActivity)?.navigateToSaved()
        }
    }

    private fun setupObservers() {
        viewModel.homeData.observe(viewLifecycleOwner) { data ->
            data?.let {
                // 1. Welcome (User Name)
                binding.tvUserNameHome.text = it.user_name

                // 2. Stats Cards
                binding.totalCasesText.text = (it.total_cases ?: 0).toString()

                // Pending cases — calculation requested: ((data.total_cases ?: 0) - 0)
                binding.pendingText.text = ((it.total_cases ?: 0) - 0).toString()

                // Show AI Accuracy — requested format: aiAccuracyText.text = "${data.average_confidence ?: 0}%"
                binding.aiAccuracyText.text = "${it.average_confidence ?: 0}%"

                // 4 & 5. Learning Modules / Saved Cases — not returned by /dashboard
                modulesAdapter.updateData(emptyList())
                casesAdapter.updateData(emptyList())
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadData() {
        // Support both "user_profile" and "UserPrefs" for userId retrieval
        val sharedPrefs = requireContext().getSharedPreferences("user_profile", Context.MODE_PRIVATE)
        val userPrefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        
        val userId = sharedPrefs.getInt("user_id", -1).let { 
            if (it == -1) userPrefs.getInt("user_id", -1) else it 
        }

        if (userId != -1) {
            viewModel.fetchHomeData(userId)
        } else {
            Toast.makeText(requireContext(), "User session not found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
