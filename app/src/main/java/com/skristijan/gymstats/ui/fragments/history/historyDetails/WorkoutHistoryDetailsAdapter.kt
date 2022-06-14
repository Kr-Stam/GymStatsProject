package com.skristijan.gymstats.ui.fragments.history.historyDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skristijan.gymstats.data.models.Exercise
import com.skristijan.gymstats.databinding.ItemHistoryDetailsBinding

class WorkoutHistoryDetailsAdapter(val exercises: MutableList<Exercise>) :
    RecyclerView.Adapter<WorkoutHistoryDetailsAdapter.ExerciseViewHolder>() {

    class ExerciseViewHolder(val binding: ItemHistoryDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder(
            ItemHistoryDetailsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val currentExercise = exercises[position]
        holder.apply {
            binding.apply {
                nameText.text = currentExercise.name
                if (currentExercise.notes != null)
                    noteText.text = notesToString(currentExercise.notes)
                else
                    noteText.visibility = View.GONE
                setsText.text = setsToString(currentExercise)
            }
        }
    }

    private fun notesToString(notes: List<String>): String {
        val sb = StringBuilder()
        for (note in notes) {
            sb.append("$note\n")
        }
        sb.removeSuffix("\n")
        return sb.toString()
    }

    private fun setsToString(exercise: Exercise): String {
        val sb = StringBuilder()
        var i = 0
        //Add checks for cardio, different types of sets and kg/lbs
        for (set in exercise.sets) {
            i++
            sb.append("$i\t${set.weight}kgx${set.reps}\n")
        }
        sb.removeSuffix("\n")
        return sb.toString()
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    fun swapData(newExercises: List<Exercise>) {
        this.exercises.clear()
        this.exercises.addAll(newExercises)
        notifyDataSetChanged()
    }
}