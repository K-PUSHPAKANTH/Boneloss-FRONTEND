package com.simats.boneloss

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simats.boneloss.databinding.ItemSavedCaseBinding

class SavedCasesAdapter(private var cases: List<SavedCase>) :
    RecyclerView.Adapter<SavedCasesAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemSavedCaseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSavedCaseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val case = cases[position]
        holder.binding.tvCaseTitle.text = case.title
        holder.binding.tvCaseInfo.text = "${case.boneLossPercent}% loss • Saved at ${case.savedAt.replace("T", " ")}"
    }

    override fun getItemCount() = cases.size

    fun updateData(newCases: List<SavedCase>) {
        cases = newCases
        notifyDataSetChanged()
    }
}
