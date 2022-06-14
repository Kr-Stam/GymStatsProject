package com.skristijan.gymstats.ui.fragments.workouts.editWorkout

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.skristijan.gymstats.data.models.Exercise
import com.skristijan.gymstats.data.models.ExerciseSet
import com.skristijan.gymstats.databinding.ItemEditWorkoutSetBinding

class EWSetsAdapter
    (
    private val sets: MutableList<ExerciseSet>,
    private val mContext: Context
) : RecyclerView.Adapter<EWSetsAdapter.SetsViewHolder>() {

    class SetsViewHolder(val binding: ItemEditWorkoutSetBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetsViewHolder {
        return SetsViewHolder(
            ItemEditWorkoutSetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SetsViewHolder, position: Int) {
        var currentSet = sets[position]
        holder.binding.apply {
            setNumber.text = (position + 1).toString()
            weight.text = Editable.Factory.getInstance().newEditable(currentSet.weight.toString())
            reps.text = Editable.Factory.getInstance().newEditable(currentSet.reps.toString())
            //Is this necessary?
            weight.addTextChangedListener {
                if (it.toString() == "")
                    weightListener?.invoke(position, 0L)
                else
                    weightListener?.invoke(position, it.toString().toLong())
            }

            reps.addTextChangedListener {
                if (it.toString() == "")
                    repListener?.invoke(position, 0)
                else
                    repListener?.invoke(position, it.toString().toInt())
            }
        }
    }

    override fun getItemCount(): Int {
        return sets.size
    }

    fun swapData(newSets: List<ExerciseSet>) {
        sets.clear()
        sets.addAll(newSets)
        notifyDataSetChanged()
    }

    fun addSet(set: ExerciseSet) {
        sets.add(set)
        notifyItemInserted(sets.size - 1)
    }

    private var listener: ((Int, ExerciseSet) -> Unit)? = null

    fun setOnCheckedListener(listener: ((Int, ExerciseSet) -> Unit)?) {
        this.listener = listener
    }

    private var deleteListener: ((Int) -> Unit)? = null

    fun setDeleteListener(listener: ((Int) -> Unit)?) {
        this.deleteListener = listener
    }

    private var undoListener: ((Int, ExerciseSet) -> Unit)? = null

    fun setUndoListener(listener: (Int, ExerciseSet) -> Unit) {
        this.undoListener = listener
    }

    private var repListener: ((Int, Int) -> Unit)? = null

    fun setRepListener(listener: (Int, Int) -> Unit) {
        this.repListener = listener
    }

    private var weightListener: ((Int, Long) -> Unit)? = null

    fun setWeightListener(listener: (Int, Long) -> Unit) {
        this.weightListener = listener
    }

    private var removedPosition: Int = -1
    private var removedSet: ExerciseSet? = null

    fun removeSet(viewHolder: RecyclerView.ViewHolder) {
        removedPosition = viewHolder.adapterPosition
        removedSet = sets[viewHolder.adapterPosition]

        sets.removeAt(viewHolder.adapterPosition)
        deleteListener?.invoke(removedPosition)
        notifyDataSetChanged()

        Snackbar.make(viewHolder.itemView, "Set deleted", Snackbar.LENGTH_LONG).setAction("UNDO") {
            if (removedSet != null) {
                sets.add(removedPosition, removedSet!!)
                notifyItemInserted(removedPosition)
                undoListener?.invoke(removedPosition, removedSet!!)
            }
        }.show()
    }
}