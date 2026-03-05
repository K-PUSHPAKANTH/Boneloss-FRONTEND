package com.simats.boneloss

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.simats.boneloss.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityHomeBinding
    private val viewModel: DashboardViewModel by viewModels()
    private var userId: Int = -1
    private lateinit var adapter: RecentActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        observeViewModel()

        // Get user_id from SharedPreferences
        val sharedPrefs = getSharedPreferences("user_profile", Context.MODE_PRIVATE)
        userId = sharedPrefs.getInt("user_id", -1)

        if (userId != -1) {
            android.util.Log.d("DashboardDebug", "Fetching data for userId: $userId")
            viewModel.fetchDashboardData(userId)
        } else {
            android.util.Log.e("DashboardDebug", "UserId is -1. User might not be logged in or ID not saved.")
            Toast.makeText(this, "User not logged in correctly", Toast.LENGTH_SHORT).show()
        }

        // Setup Welcome Text
        val savedName = sharedPrefs.getString("full_name", "User")
        val userName = intent.getStringExtra("user_name") ?: savedName
        binding.tvUserName.text = userName
    }

    private fun setupRecyclerView() {
        adapter = RecentActivityAdapter(emptyList())
        binding.rvRecentActivity.layoutManager = LinearLayoutManager(this)
        binding.rvRecentActivity.adapter = adapter
    }

    private fun setupListeners() {
        // Notification Bell
        binding.btnNotification.setOnClickListener {
            val intent = Intent(this, NotificationsSettingsActivity::class.java)
            startActivity(intent)
        }

        // Action Buttons
        binding.btnUploadXray.setOnClickListener {
            val intent = Intent(this, UploadRadiographActivity::class.java)
            startActivity(intent)
        }

        binding.btnScanImage.setOnClickListener {
            val intent = Intent(this, UploadRadiographActivity::class.java)
            startActivity(intent)
        }

        binding.btnViewAll.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putExtra("user_id", userId)
            startActivity(intent)
        }

        // Bottom Navigation
        binding.navAnalyticsBottom.setOnClickListener {
            val intent = Intent(this, AnalyticsActivity::class.java)
            startActivity(intent)
        }

        binding.navProfile.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.navFiles.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putExtra("user_id", userId)
            startActivity(intent)
        }

        binding.navFab.setOnClickListener {
            val intent = Intent(this, UploadRadiographActivity::class.java)
            startActivity(intent)
        }
        
        binding.navHome.setOnClickListener {
            // Already on Home
        }
    }

    private fun observeViewModel() {
        viewModel.dashboardData.observe(this) { data ->
            data?.let {
                binding.tvTotalCases.text = (it.total_cases ?: 0).toString()
                binding.tvCompletedCases.text = (it.total_cases ?: 0).toString()
                binding.tvAvgBoneLoss.text = String.format("%.1f%%", it.average_confidence ?: 0.0)
                
                // Recent activity removed from dashboard response for now
                // adapter.updateData(it.recentActivity)
                
                // Update welcome name if backend returns different
                if (!it.user_name.isNullOrEmpty()) {
                    binding.tvUserName.text = it.user_name
                }
                
                binding.tvErrorDebug.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.pbDashboard.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                binding.tvErrorDebug.text = "Connection Status: $it\n(Check if server is running)"
                binding.tvErrorDebug.visibility = View.VISIBLE
            }
        }
    }
}
