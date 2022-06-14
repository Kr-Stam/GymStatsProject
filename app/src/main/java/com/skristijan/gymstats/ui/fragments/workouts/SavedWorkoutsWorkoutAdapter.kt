package com.skristijan.gymstats.ui.fragments.workouts

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.skristijan.gymstats.R
import com.skristijan.gymstats.data.models.Exercise
import com.skristijan.gymstats.data.room.entities.SavedWorkout
import com.skristijan.gymstats.databinding.ItemProgramBinding
import com.skristijan.gymstats.databinding.ItemSavedWorkoutBinding
import java.lang.StringBuilder

class SavedWorkoutsWorkoutAdapter(private val workouts: MutableList<SavedWorkout>): RecyclerView.Adapter<SavedWorkoutsWorkoutAdapter.SavedWorkoutViewHolder>() {

    class SavedWorkoutViewHolder(val binding: ItemSavedWorkoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedWorkoutViewHolder {
        return SavedWorkoutViewHolder(
            ItemSavedWorkoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SavedWorkoutViewHolder, position: Int) {
        val currentWorkout = workouts[position]
        holder.apply {
            binding.apply {
                nameText.text = currentWorkout.workout.name
                savedWorkoutExercisesText.text = parseExercises(currentWorkout.workout.exercises)

                itemView.setOnClickListener {
                    Log.d("SHIT", "WORKS")
                    listener?.invoke(currentWorkout)
                }

                //TODO ADD OPTIONS MENU
                options.setOnClickListener {
                    val menu = PopupMenu(it.context, it)
                    menu.inflate(R.menu.saved_workout_options_menu)
                    menu.show()
                    menu.setOnMenuItemClickListener { menu ->
                        when(menu.itemId){
                            R.id.addToProgramMenu -> {
                                //TODO IMPLEMENT
                                //Add listener that returns operation code/string and selected workout
                                //Then act on that listener in the fragment
                                optionsListener?.invoke("program", currentWorkout, position)
                                true
                            }
                            R.id.deleteMenu -> {
                                optionsListener?.invoke("delete", currentWorkout, position)
                                true
                            }
                            R.id.duplicateMenu -> {
                                //TODO DUPLICATE BELLOW ORIGINAL
                                optionsListener?.invoke("duplicate", currentWorkout, position)
                                true
                            }
                            R.id.editMenu -> {
                                //TODO IMPLEMENT
                                optionsListener?.invoke("edit", currentWorkout, position)
                                true
                            }
                            else -> true
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return workouts.size
    }

    private var listener: ((SavedWorkout) -> Unit)? = null

    fun setOnSavedWorkoutTapListener(listener: ((SavedWorkout) -> Unit)?) {
        this.listener = listener
    }

    private var optionsListener: ((String, SavedWorkout, Int) -> Unit)? = null

    fun setOptionsListener(listener: ((String, SavedWorkout, Int) -> Unit)?){
        this.optionsListener = listener
    }

    fun swapData(newWorkouts: List<SavedWorkout>) {
        this.workouts.clear()
        this.workouts.addAll(newWorkouts)
        notifyDataSetChanged()
    }

    private fun parseExercises(exercises: List<Exercise>): String {
        val sb = StringBuilder()
        exercises.forEach {
            sb.append("${it.sets.size} x ${it.name}\n")
        }
        sb.removeSuffix("\n")
        return sb.toString()
    }

}