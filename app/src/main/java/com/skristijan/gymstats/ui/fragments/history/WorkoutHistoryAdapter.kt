package com.skristijan.gymstats.ui.fragments.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.skristijan.gymstats.R
import com.skristijan.gymstats.data.models.Exercise
import com.skristijan.gymstats.data.models.Workout
import com.skristijan.gymstats.data.room.entities.SavedWorkout
import com.skristijan.gymstats.data.room.entities.WorkoutHistory
import com.skristijan.gymstats.databinding.ItemWorkoutHistoryBinding
import java.lang.StringBuilder

class WorkoutHistoryAdapter(private val history: MutableList<WorkoutHistory>) :
    RecyclerView.Adapter<WorkoutHistoryAdapter.WorkoutHistoryViewHolder>() {

    class WorkoutHistoryViewHolder(val binding: ItemWorkoutHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutHistoryViewHolder {
        return WorkoutHistoryViewHolder(
            ItemWorkoutHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WorkoutHistoryViewHolder, position: Int) {
        val currentWorkoutHistory = history[position]
        holder.apply {
            binding.apply {
                nameText.text = currentWorkoutHistory.workout.name
                dateText.text = currentWorkoutHistory.date
                timeText.text = parseTime(currentWorkoutHistory.time)
                binding.exercisesText.text = parseExercises(currentWorkoutHistory.workout.exercises)
               //May implement volume later

                itemView.setOnClickListener {
                    listener?.invoke(currentWorkoutHistory)
                }

                options.setOnClickListener {
                    val menu = PopupMenu(it.context, it)
                    menu.inflate(R.menu.workout_history_options_menu)
                    menu.show()
                    menu.setOnMenuItemClickListener { menu ->
                        when(menu.itemId){
                            R.id.deleteMenu -> {
                                optionsListener?.invoke("delete", currentWorkoutHistory, position)
                                true
                            }
                            R.id.editMenu -> {
                                optionsListener?.invoke("edit", currentWorkoutHistory, position)
                                true
                            }
                            R.id.templateMenu -> {
                                //TODO IMPLEMENT IN FRAGMENT VIA ALERTDIALOG IMPL
                                templateListener?.invoke("edit", currentWorkoutHistory.workout, position)
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
        return history.size
    }

    private var listener: ((WorkoutHistory) -> Unit)? = null

    fun setOnHistoryTapListener(listener: ((WorkoutHistory) -> Unit)) {
        this.listener = listener
    }

    fun swapData(newHistory: List<WorkoutHistory>) {
        this.history.clear()
        this.history.addAll(newHistory)
        notifyDataSetChanged()
    }

    private fun parseTime(time: Long): String {
        val sb = StringBuilder()
        var tempTime = time
        if ((tempTime + 1) / 3600 > 0) {
            sb.append("${tempTime / 3600}h ")
            tempTime %= 3600
            if (time > 0)
                sb.append("${tempTime / 60}m")
        } else if ((tempTime + 1) / 60 > 0) {
            sb.append("${tempTime / 60}m ")
            tempTime %= 60
            if (time > 0)
                sb.append("${tempTime}s")
        } else {
            sb.append("${tempTime}s")
        }

        return sb.toString()
    }

    private fun parseExercises(exercises: List<Exercise>): String {
        val sb = StringBuilder()
        exercises.forEach{
            sb.append("${it.sets.size} x ${it.name}\n")
        }
        sb.removeSuffix("\n")
        return sb.toString()
    }

    private var optionsListener: ((String, WorkoutHistory, Int) -> Unit)? = null

    fun setOptionsListener(listener: ((String, WorkoutHistory, Int) -> Unit)?){
        this.optionsListener = listener
    }

    private var templateListener: ((String, Workout, Int) -> Unit)? = null

    fun setTemplateListener(listener: ((String, Workout, Int) -> Unit)?){
        this.templateListener = listener
    }

}