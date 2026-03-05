package com.simats.boneloss

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.simats.boneloss.databinding.ActivityAdminDashboardBinding

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding
    private val viewModel: AdminViewModel by viewModels {
        AdminViewModelFactory(AdminRepository(ApiClient.apiService))
    }
    private lateinit var alertsAdapter: AdminAlertsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()
        
        viewModel.fetchDashboardData()
    }

    private fun setupRecyclerView() {
        alertsAdapter = AdminAlertsAdapter(emptyList())
        binding.rvAlerts.apply {
            layoutManager = LinearLayoutManager(this@AdminDashboardActivity)
            adapter = alertsAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.dashboardData.observe(this) { response ->
            response?.let {
                // Bind Metrics
                binding.tvTotalUsers.text = it.metrics.totalUsers.toString()
                binding.tvTotalCases.text = it.metrics.totalCases.toString()
                binding.tvAiAccuracy.text = String.format("%.1f%%", it.metrics.aiAccuracy)
                binding.tvActiveUsers.text = it.userActivity.activeUsers24h.toString()

                // Bind Alerts
                alertsAdapter.updateData(it.alerts)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}
