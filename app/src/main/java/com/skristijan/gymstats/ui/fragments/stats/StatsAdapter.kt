package com.skristijan.gymstats.ui.fragments.stats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skristijan.gymstats.R
import com.skristijan.gymstats.databinding.ItemExerciseInfoBinding
import com.skristijan.gymstats.databinding.ItemSimpleTextViewBinding

class StatsAdapter(val stats: MutableList<String>) :
    RecyclerView.Adapter<StatsAdapter.StatViewHolder>() {

    class StatViewHolder(val binding: ItemSimpleTextViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatViewHolder {
        return StatViewHolder(
            ItemSimpleTextViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StatViewHolder, position: Int) {
        holder.binding.apply {
            textView.text = stats[position]
        }
        holder.itemView.setOnClickListener {
            listener?.invoke(stats[position])
        }
    }


    override fun getItemCount(): Int {
        return stats.size
    }

    fun swapData(newData: List<String>) {
        stats.clear()
        stats.addAll(newData)
        notifyDataSetChanged()
    }

    var listener: ((String) -> Unit)? = null

    fun setOnStatClickListener(listener: ((String) -> Unit)?){
        this.listener = listener
    }
}