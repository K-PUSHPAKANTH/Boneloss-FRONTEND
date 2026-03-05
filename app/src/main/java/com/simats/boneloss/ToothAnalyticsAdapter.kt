package com.simats.boneloss

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ToothAnalyticsAdapter(private var toothList: List<ToothAnalytic>) :
    RecyclerView.Adapter<ToothAnalyticsAdapter.ToothViewHolder>() {

    class ToothViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvToothCircle: TextView = view.findViewById(R.id.tv_tooth_circle)
        val tvToothIdText: TextView = view.findViewById(R.id.tv_tooth_id_text)
        val tvRootDetail: TextView = view.findViewById(R.id.tv_root_detail)
        val tvLossPercentVal: TextView = view.findViewById(R.id.tv_loss_percent_val)
        val tvLossMmVal: TextView = view.findViewById(R.id.tv_loss_mm_val)
        val pbLossIndicator: ProgressBar = view.findViewById(R.id.pb_loss_indicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToothViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tooth_measurement, parent, false)
        return ToothViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToothViewHolder, position: Int) {
        val tooth = toothList[position]
        holder.tvToothCircle.text = tooth.tooth?.toString() ?: "?"
        holder.tvToothIdText.text = if (tooth.tooth != null) "#${tooth.tooth}" else "N/A"
        holder.tvRootDetail.text = "${String.format("%.1f", tooth.rootLengthMm ?: 0.0)}mm"
        holder.tvLossPercentVal.text = "${String.format("%.1f", tooth.boneLossPercent ?: 0.0)}%"
        holder.tvLossMmVal.text = "${String.format("%.1f", tooth.boneLossMm ?: 0.0)}mm"
        
        val progress = (tooth.boneLossPercent ?: 0.0).toInt()
        holder.pbLossIndicator.progress = progress

        // Color coding based on severity
        val color = when {
            progress >= 40 -> "#EF4444" // Red (Severe)
            progress >= 25 -> "#F97316" // Orange (Moderate)
            progress >= 15 -> "#EAB308" // Yellow (Mild)
            else -> "#22C55E"           // Green (Normal)
        }
        holder.tvLossPercentVal.setTextColor(Color.parseColor(color))
    }

    override fun getItemCount() = toothList.size

    fun updateData(newList: List<ToothAnalytic>) {
        toothList = newList
        notifyDataSetChanged()
    }
}
