package com.simats.boneloss

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var rvHistory: RecyclerView
    private lateinit var adapter: HistoryAdapter
    private lateinit var pbLoading: ProgressBar
    private lateinit var llEmptyState: LinearLayout
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        userId = intent.getIntExtra("user_id", -1)
        if (userId == -1) {
            val sharedPrefs = getSharedPreferences("user_profile", MODE_PRIVATE)
            userId = sharedPrefs.getInt("user_id", -1)
        }

        if (userId == -1) {
            Toast.makeText(this, "Invalid User ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        initViews()
        fetchHistory()
    }

    private fun initViews() {
        rvHistory = findViewById(R.id.rv_history)
        pbLoading = findViewById(R.id.pb_loading)
        llEmptyState = findViewById(R.id.ll_empty_state)
        swipeRefresh = findViewById(R.id.swipe_refresh)

        adapter = HistoryAdapter(emptyList())
        rvHistory.layoutManager = LinearLayoutManager(this)
        rvHistory.adapter = adapter

        findViewById<View>(R.id.btn_back).setOnClickListener { finish() }

        swipeRefresh.setOnRefreshListener {
            fetchHistory()
        }
    }

    private fun fetchHistory() {
        if (!swipeRefresh.isRefreshing) {
            pbLoading.visibility = View.VISIBLE
        }
        llEmptyState.visibility = View.GONE
        rvHistory.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.getHistory(userId)
                
                if (response.isSuccessful && response.body() != null) {
                    val history = response.body()!!
                    if (history.isEmpty()) {
                        llEmptyState.visibility = View.VISIBLE
                        rvHistory.visibility = View.GONE
                    } else {
                        adapter.updateData(history)
                        rvHistory.visibility = View.VISIBLE
                        llEmptyState.visibility = View.GONE
                    }
                } else {
                    Toast.makeText(this@HistoryActivity, "No history found", Toast.LENGTH_SHORT).show()
                    llEmptyState.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                Toast.makeText(this@HistoryActivity, "Connection failed: ${e.message}", Toast.LENGTH_SHORT).show()
                llEmptyState.visibility = View.VISIBLE
            } finally {
                pbLoading.visibility = View.GONE
                swipeRefresh.isRefreshing = false
            }
        }
    }
}
