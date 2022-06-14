package com.skristijan.gymstats.ui.main.currentWorkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.skristijan.gymstats.data.models.ExerciseSet
import com.skristijan.gymstats.databinding.ItemCurrentWorkoutSetsBinding

class CWSetsAdapter(private val sets: MutableList<ExerciseSet>) :
    RecyclerView.Adapter<CWSetsAdapter.SetsViewHolder>() {

    class SetsViewHolder(val binding: ItemCurrentWorkoutSetsBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetsViewHolder {
        return SetsViewHolder(
            ItemCurrentWorkoutSetsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SetsViewHolder, position: Int) {
        var currentSet = sets[position]
        var finalWeight: Long = currentSet.weight
        var finalReps: Int = currentSet.reps
        holder.binding.apply {
            reps.hint = currentSet.reps.toString()
            setNumber.text = (position + 1).toString()
            weight.hint = currentSet.weight.toString()
            //Is this necessary?
//            checkBoxOnSetComplete.isChecked = currentSet.finished
//            checkBoxOnSetComplete.setOnCheckedChangeListener { buttonView, isChecked ->
//                if(weight.text.toString() != "")
//                    finalWeight = weight.text.toString().toLong()
//                if(reps.text.toString() != "")
//                    finalReps = reps.text.toString().toInt()
//                //TODO ADD TYPE SELECTOR
//                sets[position] = ExerciseSet(finalWeight, finalReps, currentSet.type, isChecked)
//                currentSet = sets[position]
//                weight.hint = currentSet.weight.toString()
//                reps.hint = currentSet.reps.toString()
//                listener?.invoke(position, currentSet)
//            }
        }
    }

    override fun getItemCount(): Int {
        return sets.size
    }

    fun swapData(newSets: List<ExerciseSet>){
        this.sets.clear()
        this.sets.addAll(newSets)
        notifyDataSetChanged()
    }

    fun addSet(newSet: ExerciseSet){
        this.sets.add(newSet)
        notifyItemInserted(sets.size - 1)
    }

    private var listener: ((Int, ExerciseSet) -> Unit)? = null

    fun setOnCheckedListener(listener: ((Int, ExerciseSet) -> Unit)?){
        this.listener = listener
    }

    private var deleteListener: ((Int) -> Unit)? = null

    fun setDeleteListener(listener: ((Int) -> Unit)?){
        this.deleteListener = listener
    }

    private var undoListener: ((Int, ExerciseSet) -> Unit)? = null

    fun setUndoListener(listener: (Int, ExerciseSet) -> Unit){
        this.undoListener = listener
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