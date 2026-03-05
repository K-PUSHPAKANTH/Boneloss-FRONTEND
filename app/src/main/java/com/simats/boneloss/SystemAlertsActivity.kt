package com.simats.boneloss

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SystemAlertsActivity : AppCompatActivity() {

    private lateinit var rvAlerts: RecyclerView
    private lateinit var btnBack: ImageView
    private lateinit var tvAlertCount: TextView

    private var currentFilter = "ALL"
    private val allAlerts = mutableListOf<SystemAlert>()
    private lateinit var adapter: SystemAlertAdapter

    // Tab views
    private lateinit var tabAll: TextView
    private lateinit var tabCritical: TextView
    private lateinit var tabWarning: TextView
    private lateinit var tabInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_system_alerts)

        btnBack = findViewById(R.id.btn_back)
        rvAlerts = findViewById(R.id.rv_system_alerts)
        tvAlertCount = findViewById(R.id.tv_alert_count)

        tabAll = findViewById(R.id.tab_all)
        tabCritical = findViewById(R.id.tab_critical)
        tabWarning = findViewById(R.id.tab_warning)
        tabInfo = findViewById(R.id.tab_info)

        btnBack.setOnClickListener { finish() }

        allAlerts.addAll(getSystemAlerts())
        adapter = SystemAlertAdapter(allAlerts.toMutableList())
        rvAlerts.layoutManager = LinearLayoutManager(this)
        rvAlerts.adapter = adapter

        updateAlertCount()
        setupFilterTabs()
    }

    private fun setupFilterTabs() {
        setTabSelected(tabAll)

        tabAll.setOnClickListener {
            currentFilter = "ALL"
            setTabSelected(tabAll)
            filterAlerts()
        }
        tabCritical.setOnClickListener {
            currentFilter = "CRITICAL"
            setTabSelected(tabCritical)
            filterAlerts()
        }
        tabWarning.setOnClickListener {
            currentFilter = "WARNING"
            setTabSelected(tabWarning)
            filterAlerts()
        }
        tabInfo.setOnClickListener {
            currentFilter = "INFO"
            setTabSelected(tabInfo)
            filterAlerts()
        }
    }

    private fun setTabSelected(selected: TextView) {
        val tabs = listOf(tabAll, tabCritical, tabWarning, tabInfo)
        tabs.forEach { tab ->
            if (tab == selected) {
                tab.setBackgroundResource(R.drawable.white_rounded_square)
                tab.setTextColor(Color.parseColor("#8B5CF6"))
                tab.typeface = android.graphics.Typeface.DEFAULT_BOLD
            } else {
                tab.setBackgroundResource(R.drawable.bg_icon_button_white)
                tab.backgroundTintList = android.content.res.ColorStateList.valueOf(
                    Color.parseColor("#33FFFFFF")
                )
                tab.setTextColor(Color.parseColor("#E0E7FF"))
                tab.typeface = android.graphics.Typeface.DEFAULT
            }
        }
    }

    private fun filterAlerts() {
        val filtered = if (currentFilter == "ALL") {
            allAlerts.toMutableList()
        } else {
            allAlerts.filter { it.severity == currentFilter }.toMutableList()
        }
        adapter.updateData(filtered)
        updateAlertCount(filtered.size)
    }

    private fun updateAlertCount(count: Int = allAlerts.size) {
        tvAlertCount.text = "$count Active"
    }

    private fun getSystemAlerts(): List<SystemAlert> {
        return listOf(
            SystemAlert(
                title = "High Processing Volume",
                message = "45 cases currently in the analysis queue. Processing may be delayed.",
                time = "15 min ago",
                severity = "CRITICAL",
                iconResId = R.drawable.ic_report,
                action = "Monitor",
                severityColor = "#EF4444",
                iconTint = "#D97706",
                iconBgTint = "#FEF3C7"
            ),
            SystemAlert(
                title = "Security Alert",
                message = "Unusual login activity detected from IP 192.168.1.105. Please review.",
                time = "30 min ago",
                severity = "CRITICAL",
                iconResId = R.drawable.ic_shield_check_outline,
                action = "Review",
                severityColor = "#EF4444",
                iconTint = "#DC2626",
                iconBgTint = "#FEE2E2"
            ),
            SystemAlert(
                title = "Storage Warning",
                message = "Database storage at 78% capacity. Consider archiving older records.",
                time = "1 hour ago",
                severity = "WARNING",
                iconResId = R.drawable.ic_analytics,
                action = "View",
                severityColor = "#F59E0B",
                iconTint = "#D97706",
                iconBgTint = "#FEF3C7"
            ),
            SystemAlert(
                title = "Slow API Response",
                message = "AI model endpoint averaging 4.2s response time. Normal threshold is 2s.",
                time = "2 hours ago",
                severity = "WARNING",
                iconResId = R.drawable.ic_clock,
                action = "Debug",
                severityColor = "#F59E0B",
                iconTint = "#D97706",
                iconBgTint = "#FEF3C7"
            ),
            SystemAlert(
                title = "AI Model Updated",
                message = "Bone loss detection model v3.2.1 has been successfully deployed.",
                time = "3 hours ago",
                severity = "INFO",
                iconResId = R.drawable.ic_upload_blue,
                action = "Details",
                severityColor = "#3B82F6",
                iconTint = "#3B82F6",
                iconBgTint = "#DBEAFE"
            ),
            SystemAlert(
                title = "System Health Check",
                message = "Scheduled health check completed. All systems are operational.",
                time = "1 hour ago",
                severity = "INFO",
                iconResId = R.drawable.ic_check_circle,
                action = "Report",
                severityColor = "#10B981",
                iconTint = "#10B981",
                iconBgTint = "#D1FAE5"
            ),
            SystemAlert(
                title = "Scheduled Maintenance",
                message = "System maintenance window scheduled for tonight at 02:00 AM UTC.",
                time = "5 hours ago",
                severity = "INFO",
                iconResId = R.drawable.ic_settings,
                action = "View",
                severityColor = "#6B7280",
                iconTint = "#6B7280",
                iconBgTint = "#F3F4F6"
            ),
            SystemAlert(
                title = "Weekly Backup Complete",
                message = "Weekly data backup completed successfully. All records are secured.",
                time = "1 day ago",
                severity = "INFO",
                iconResId = R.drawable.ic_folder_open,
                action = "View",
                severityColor = "#10B981",
                iconTint = "#10B981",
                iconBgTint = "#D1FAE5"
            )
        )
    }

    // Data class
    data class SystemAlert(
        val title: String,
        val message: String,
        val time: String,
        val severity: String,       // "CRITICAL", "WARNING", "INFO"
        val iconResId: Int,
        val action: String,
        val severityColor: String,
        val iconTint: String,
        val iconBgTint: String
    )

    // Adapter
    class SystemAlertAdapter(private var items: MutableList<SystemAlert>) :
        RecyclerView.Adapter<SystemAlertAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val severityStrip: View = view.findViewById(R.id.view_severity_strip)
            val ivIcon: ImageView = view.findViewById(R.id.iv_alert_icon)
            val tvTitle: TextView = view.findViewById(R.id.tv_alert_title)
            val tvSeverity: TextView = view.findViewById(R.id.tv_alert_severity)
            val tvMessage: TextView = view.findViewById(R.id.tv_alert_message)
            val tvTime: TextView = view.findViewById(R.id.tv_alert_time)
            val tvAction: TextView = view.findViewById(R.id.tv_alert_action)
            val iconFrame: android.widget.FrameLayout = view.findViewById(R.id.fl_alert_icon_bg)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_system_alert, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.tvTitle.text = item.title
            holder.tvMessage.text = item.message
            holder.tvTime.text = item.time
            holder.tvSeverity.text = item.severity
            holder.tvAction.text = item.action
            holder.ivIcon.setImageResource(item.iconResId)

            val severityColor = Color.parseColor(item.severityColor)
            val iconTintColor = Color.parseColor(item.iconTint)
            val iconBgColor = Color.parseColor(item.iconBgTint)

            holder.severityStrip.setBackgroundColor(severityColor)
            holder.tvSeverity.backgroundTintList = android.content.res.ColorStateList.valueOf(severityColor)
            holder.ivIcon.setColorFilter(iconTintColor)
            holder.iconFrame.backgroundTintList = android.content.res.ColorStateList.valueOf(iconBgColor)
        }

        override fun getItemCount() = items.size

        fun updateData(newItems: MutableList<SystemAlert>) {
            items = newItems
            notifyDataSetChanged()
        }
    }
}
