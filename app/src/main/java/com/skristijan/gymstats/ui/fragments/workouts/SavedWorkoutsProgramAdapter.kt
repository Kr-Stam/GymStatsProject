package com.skristijan.gymstats.ui.fragments.workouts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.skristijan.gymstats.R
import com.skristijan.gymstats.data.models.Exercise
import com.skristijan.gymstats.data.models.Program
import com.skristijan.gymstats.data.models.Workout
import com.skristijan.gymstats.data.room.entities.SavedWorkout
import com.skristijan.gymstats.data.room.entities.WorkoutHistory
import com.skristijan.gymstats.databinding.ItemProgramCustomBinding
import com.skristijan.gymstats.ui.fragments.history.WorkoutHistoryAdapter
import java.lang.StringBuilder

class SavedWorkoutsProgramAdapter(private val programs: MutableList<Program>) :
    RecyclerView.Adapter<SavedWorkoutsProgramAdapter.ProgramViewHolder>() {

    class ProgramViewHolder(val binding: ItemProgramCustomBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgramViewHolder {
        return ProgramViewHolder(
            ItemProgramCustomBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProgramViewHolder, position: Int) {
        val program = programs[position]
        val itemTouchHelper = ItemTouchHelper(dragCallback)
        itemTouchHelper.attachToRecyclerView(holder.binding.workoutsRV)
        holder.apply {
            binding.apply {
                programNameText.text = program.name
                val adapter = SavedWorkoutsWorkoutAdapter(mutableListOf())

                workoutsRV.adapter = adapter
                adapter.swapData(program.workouts)
                if (program.isExpanded) {
                    arrow.setImageResource(R.drawable.ic_arrow_up)
                    workoutsRV.visibility = View.VISIBLE
                } else {
                    arrow.setImageResource(R.drawable.ic_arrow_down)
                    workoutsRV.visibility = View.GONE
                }
                programBarContainer.setOnClickListener {
//                    headerListener?.invoke(position)
                    programs[position].isExpanded = !program.isExpanded
                    notifyItemChanged(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return programs.size
    }

    private var listener: ((SavedWorkout) -> Unit)? = null

    fun setOnSavedWorkoutTapListener(listener: ((SavedWorkout) -> Unit)?) {
        this.listener = listener
    }

    private var headerListener: ((Int) -> Unit)? = null

    fun setOnProgramTapListener(listener: ((Int) -> Unit)?) {
        this.headerListener = listener
    }

    private var optionsListener: ((String, SavedWorkout, Int) -> Unit)? = null

    fun setOptionsListener(listener: ((String, SavedWorkout, Int) -> Unit)?) {
        this.optionsListener = listener
    }

    fun swapData(newData: MutableList<Program>) {
        this.programs.clear()
        this.programs.addAll(newData)
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

    private val dragCallback = DragHelper()

    //TODO IMPLEMENT PROPERLY
    private class DragHelper : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition

//            Collections.swap()


            return false
        }


        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            TODO("Not yet implemented")
        }
    }

}