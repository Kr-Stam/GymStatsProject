package com.skristijan.gymstats.ui.fragments.workouts.workoutDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skristijan.gymstats.R
import com.skristijan.gymstats.data.models.Exercise
import com.skristijan.gymstats.databinding.ItemSavedWorkoutsDetailsExerciseBinding

class SavedWorkoutDetailsAdapter(val exercises: MutableList<Exercise>) :
    RecyclerView.Adapter<SavedWorkoutDetailsAdapter.ExerciseViewHolder>() {

//    var mExpanded = -1
    var mExpanded = BooleanArray(exercises.size) {
        false
    }

    class ExerciseViewHolder(val binding: ItemSavedWorkoutsDetailsExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder(
            ItemSavedWorkoutsDetailsExerciseBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val currentExercise = exercises[position]
        val expanded = mExpanded[position]
        holder.apply {
            binding.apply {
//                exerciseImage.setImageResource
                setsText.visibility = if (expanded) View.VISIBLE else View.GONE
                setsText.text = setsToString(currentExercise)
                exerciseText.text = currentExercise.name
                arrow.setImageResource(if (expanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down)
                arrow.setOnClickListener {
                    mExpanded[position] = !expanded
                    notifyItemChanged(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    fun swapData(newExercises: List<Exercise>) {
        this.exercises.clear()
        this.exercises.addAll(newExercises)
        mExpanded = BooleanArray(exercises.size) {
            false
        }
        notifyDataSetChanged()
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
}