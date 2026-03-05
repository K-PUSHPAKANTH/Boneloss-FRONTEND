package com.simats.boneloss

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HeatmapAdapter(private var heatmapDataList: List<HeatmapData>) :
    RecyclerView.Adapter<HeatmapAdapter.HeatmapViewHolder>() {

    class HeatmapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llToothContainer: LinearLayout = itemView.findViewById(R.id.ll_tooth_container)
        val tvToothNumber: TextView = itemView.findViewById(R.id.tv_tooth_number)
        val tvBoneLossPercent: TextView = itemView.findViewById(R.id.tv_bone_loss_percent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeatmapViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tooth_heatmap, parent, false)
        return HeatmapViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeatmapViewHolder, position: Int) {
        val data = heatmapDataList[position]
        val toothNum = data.tooth ?: 0
        val lossPercent = data.bone_loss_percent ?: 0f

        holder.tvToothNumber.text = "Tooth $toothNum"
        holder.tvBoneLossPercent.text = "$lossPercent%"

        val color = when {
            lossPercent <= 15 -> Color.parseColor("#D1FAE5") // Light Green bg
            lossPercent <= 33 -> Color.parseColor("#FEF3C7") // Light Yellow bg
            lossPercent <= 50 -> Color.parseColor("#FFEDD5") // Light Orange bg
            else -> Color.parseColor("#FEE2E2") // Light Red bg
        }

        val textColor = when {
            lossPercent <= 15 -> Color.parseColor("#065F46") // Dark Green text
            lossPercent <= 33 -> Color.parseColor("#92400E") // Dark Yellow text
            lossPercent <= 50 -> Color.parseColor("#9A3412") // Dark Orange text
            else -> Color.parseColor("#991B1B") // Dark Red text
        }

        holder.llToothContainer.setBackgroundColor(color)
        holder.tvToothNumber.setTextColor(textColor)
        holder.tvBoneLossPercent.setTextColor(textColor)
    }

    override fun getItemCount(): Int {
        return heatmapDataList.size
    }

    fun updateData(newData: List<HeatmapData>) {
        heatmapDataList = newData
        notifyDataSetChanged()
    }
}
