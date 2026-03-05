package com.simats.boneloss

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simats.boneloss.databinding.ItemRecentActivityBinding

class RecentActivityAdapter(private var items: List<RecentActivityItem>) :
    RecyclerView.Adapter<RecentActivityAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemRecentActivityBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecentActivityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            tvPatientId.text = "Patient ID: ${item.patientId}"
            tvActivityDetails.text = "${item.status} • ${item.boneLossPercent}% bone loss detected"
            
            // Simple logic to change icon color based on status if needed
            if (item.status == "Completed") {
                ivStatusIcon.setImageResource(R.drawable.ic_cardiogram)
                ivStatusIcon.setBackgroundResource(R.drawable.circle_green_light)
            } else {
                ivStatusIcon.setImageResource(R.drawable.ic_cardiogram)
                ivStatusIcon.setBackgroundResource(R.drawable.circle_yellow)
            }
        }
    }

    override fun getItemCount() = items.size

    fun updateData(newItems: List<RecentActivityItem>) {
        android.util.Log.d("DashboardDebug", "Updating adapter with ${newItems.size} items")
        items = newItems
        notifyDataSetChanged()
    }
}
