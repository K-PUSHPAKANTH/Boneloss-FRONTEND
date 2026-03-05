package com.simats.boneloss

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(private var historyList: List<HistoryResponse>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val patientId: TextView = view.findViewById(R.id.tv_patient_id)
        val predictionBadge: TextView = view.findViewById(R.id.tv_prediction_badge)
        val date: TextView = view.findViewById(R.id.tv_history_date)
        val boneLoss: TextView = view.findViewById(R.id.tv_bone_loss_percent)
        val confidence: TextView = view.findViewById(R.id.tv_confidence_score)
        val severityStrip: View = view.findViewById(R.id.view_severity_strip)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyList[position]
        holder.patientId.text = item.patient_id
        holder.predictionBadge.text = item.prediction.uppercase()
        holder.boneLoss.text = "${String.format("%.1f", item.average_bone_loss)}%"
        holder.confidence.text = "${String.format("%.1f", item.confidence)}% Conf."
        
        // Format Date
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMMM d, yyyy, h:mm a", Locale.getDefault())
            val date = inputFormat.parse(item.created_at)
            holder.date.text = outputFormat.format(date!!)
        } catch (e: Exception) {
            holder.date.text = item.created_at
        }

        // Color coding based on severity
        when (item.prediction.lowercase()) {
            "mild" -> {
                holder.severityStrip.setBackgroundColor(Color.parseColor("#10B981")) // Green
                holder.predictionBadge.setBackgroundResource(R.drawable.bg_priority_badge_green)
                holder.predictionBadge.setTextColor(Color.parseColor("#065F46"))
            }
            "moderate" -> {
                holder.severityStrip.setBackgroundColor(Color.parseColor("#F59E0B")) // Orange
                holder.predictionBadge.setBackgroundResource(R.drawable.bg_priority_badge_yellow)
                holder.predictionBadge.setTextColor(Color.parseColor("#92400E"))
            }
            "severe" -> {
                holder.severityStrip.setBackgroundColor(Color.parseColor("#EF4444")) // Red
                holder.predictionBadge.setBackgroundResource(R.drawable.bg_priority_badge_red)
                holder.predictionBadge.setTextColor(Color.parseColor("#991B1B"))
            }
            else -> {
                holder.severityStrip.setBackgroundColor(Color.GRAY)
                holder.predictionBadge.setBackgroundResource(R.drawable.bg_priority_badge)
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, FullPatientReportActivity::class.java)
            intent.putExtra("ANALYSIS_ID", item.analysis_id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = historyList.size

    fun updateData(newList: List<HistoryResponse>) {
        historyList = newList
        notifyDataSetChanged()
    }
}
