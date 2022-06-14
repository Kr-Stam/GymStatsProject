package com.skristijan.gymstats.ui.fragments.stats.statDetails

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skristijan.gymstats.data.room.entities.PR
import com.skristijan.gymstats.databinding.ItemPrHistoryBinding
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class StatHistoryAdapter(private val stats: MutableList<PR>) :
    RecyclerView.Adapter<StatHistoryAdapter.StatHistoryViewHolder>() {

    class StatHistoryViewHolder(val binding: ItemPrHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatHistoryViewHolder {
        return StatHistoryViewHolder(
            ItemPrHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StatHistoryViewHolder, position: Int) {
        val currentPr = stats[position]
        holder.binding.apply {
            dateText.text = SimpleDateFormat("MMM DD").format(Date(currentPr.date))
            valueText.text = currentPr.weight.toString() + " kg"
            timeText.text = SimpleDateFormat("HH:MM").format(Date(currentPr.date))
        }
        holder.itemView.setOnClickListener {
            holder.itemView.setBackgroundColor(Color.LTGRAY)
            listener?.invoke(currentPr, it)
        }
    }

    override fun getItemCount(): Int {
        return stats.size
    }

    private var listener: ((PR, View) -> Unit)? = null

    fun setOnHistoryTappedListener(listener:  ((PR, View) -> Unit)?){
        this.listener = listener
    }

    fun swapData(newData: List<PR>){
        stats.clear()
        stats.addAll(newData)
        notifyDataSetChanged()
    }

    private var deactivateListener: ((View) -> Unit)? = null

    fun deActivateHighlight(listener:  ((View) -> Unit)?){
        this.deactivateListener = listener
    }
}