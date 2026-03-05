package com.simats.boneloss

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simats.boneloss.databinding.ItemLearningModuleBinding

class LearningModulesAdapter(private var modules: List<LearningModule>) :
    RecyclerView.Adapter<LearningModulesAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemLearningModuleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLearningModuleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val module = modules[position]
        holder.binding.tvModuleTitle.text = module.title
        holder.binding.progressBarModule.progress = if (module.isCompleted) 100 else 0
        holder.binding.btnReview.text = if (module.isCompleted) "Review" else "Start"
        
        if (module.isCompleted) {
            holder.binding.ivModuleStatus.setImageResource(R.drawable.ic_check_filled)
            holder.binding.ivModuleStatus.backgroundTintList = holder.itemView.context.getColorStateList(R.color.success_green)
        } else {
            holder.binding.ivModuleStatus.setImageResource(R.drawable.ic_play_circle)
            holder.binding.ivModuleStatus.backgroundTintList = holder.itemView.context.getColorStateList(R.color.role_selected)
        }
    }

    override fun getItemCount() = modules.size

    fun updateData(newModules: List<LearningModule>) {
        modules = newModules
        notifyDataSetChanged()
    }
}
