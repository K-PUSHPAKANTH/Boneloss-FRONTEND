package com.simats.boneloss

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simats.boneloss.databinding.ItemAdminAlertBinding

class AdminAlertsAdapter(private var alerts: List<AdminAlert>) :
    RecyclerView.Adapter<AdminAlertsAdapter.AlertViewHolder>() {

    class AlertViewHolder(val binding: ItemAdminAlertBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val binding = ItemAdminAlertBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AlertViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val alert = alerts[position]
        holder.binding.tvAlertTitle.text = alert.title
        holder.binding.tvAlertDescription.text = alert.description
        holder.binding.tvAlertType.text = alert.type
    }

    override fun getItemCount(): Int = alerts.size

    fun updateData(newAlerts: List<AdminAlert>) {
        this.alerts = newAlerts
        notifyDataSetChanged()
    }
}
