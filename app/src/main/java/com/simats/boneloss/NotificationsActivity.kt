package com.simats.boneloss

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NotificationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        // Back Button
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }

        // RecyclerView Setup
        val rvNotifications = findViewById<RecyclerView>(R.id.rv_notifications)
        rvNotifications.layoutManager = LinearLayoutManager(this)
        rvNotifications.adapter = NotificationAdapter(getDummyNotifications())
    }

    private fun getDummyNotifications(): List<NotificationItem> {
        return listOf(
            NotificationItem("System Update", "The system will undergo maintenance at midnight.", "2m ago", true, R.drawable.ic_settings),
            NotificationItem("Analysis Complete", "Patient John Doe's analysis is ready for review.", "1h ago", true, R.drawable.ic_analytics),
            NotificationItem("New Case Assigned", "You have been assigned a new case: Patient #1245.", "3h ago", false, R.drawable.ic_folder_open),
            NotificationItem("Security Alert", "New login attempt detected from a new device.", "1d ago", false, R.drawable.ic_shield_check_outline),
            NotificationItem("Weekly Report", "Your weekly activity report is available for download.", "2d ago", false, R.drawable.ic_report)
        )
    }

    // Data Class
    data class NotificationItem(
        val title: String,
        val message: String,
        val time: String,
        val isUnread: Boolean,
        val iconResId: Int
    )

    // Adapter
    class NotificationAdapter(private val items: List<NotificationItem>) :
        RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvTitle: TextView = view.findViewById(R.id.tv_notification_title)
            val tvMessage: TextView = view.findViewById(R.id.tv_notification_message)
            val tvTime: TextView = view.findViewById(R.id.tv_notification_time)
            val ivIcon: ImageView = view.findViewById(R.id.iv_notification_icon)
            val unreadIndicator: View = view.findViewById(R.id.view_unread_indicator)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notification, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.tvTitle.text = item.title
            holder.tvMessage.text = item.message
            holder.tvTime.text = item.time
            holder.ivIcon.setImageResource(item.iconResId)

            if (item.isUnread) {
                holder.unreadIndicator.visibility = View.VISIBLE
                holder.tvTitle.setTextColor(holder.itemView.context.resources.getColor(android.R.color.black))
            } else {
                holder.unreadIndicator.visibility = View.GONE
                 holder.tvTitle.setTextColor(holder.itemView.context.resources.getColor(R.color.gray_600))
            }
        }

        override fun getItemCount() = items.size
    }
}
