package com.skristijan.gymstats.ui.fragments.workouts

import android.content.Context
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
import com.zhukic.sectionedrecyclerview.SectionedRecyclerViewAdapter
import java.lang.StringBuilder

class SavedWorkoutsAdapter(private val workouts: MutableList<SavedWorkout>) :
    SectionedRecyclerViewAdapter<SavedWorkoutsAdapter.ProgramViewHolder, SavedWorkoutsAdapter.SavedWorkoutViewHolder>() {

    class SavedWorkoutViewHolder(val binding: ItemSavedWorkoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var currentPos: Int = -1
        fun getPos(): Int {
            return currentPos
        }
    }

    class ProgramViewHolder(val binding: ItemProgramBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): SavedWorkoutViewHolder {
        Log.d("AppDebug", "Oncreate() was called")
        return SavedWorkoutViewHolder(
            ItemSavedWorkoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindItemViewHolder(holder: SavedWorkoutViewHolder, itemPosition: Int) {
        val currentWorkout = workouts[itemPosition]
        holder.currentPos = itemPosition
        holder.apply {
            binding.apply {
                nameText.text = currentWorkout.workout.name
                savedWorkoutExercisesText.text = parseExercises(currentWorkout.workout.exercises)

                itemView.setOnClickListener {
                    Log.d("CHECKTEST", "itemPos " + itemPosition)
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
                                optionsListener?.invoke("program", currentWorkout, itemPosition)
                                true
                            }
                            R.id.deleteMenu -> {
                                optionsListener?.invoke("delete", currentWorkout, itemPosition)
                                true
                            }
                            R.id.duplicateMenu -> {
                                //TODO DUPLICATE BELLOW ORIGINAL
                                optionsListener?.invoke("duplicate", currentWorkout, itemPosition)
                                true
                            }
                            R.id.editMenu -> {
                                //TODO IMPLEMENT
                                optionsListener?.invoke("edit", currentWorkout, itemPosition)
                                true
                            }
                            else -> true
                        }
                    }
                }
            }
        }
    }

    override fun onCreateSubheaderViewHolder(parent: ViewGroup, viewType: Int): ProgramViewHolder {
        return ProgramViewHolder(
            ItemProgramBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindSubheaderViewHolder(
        subheaderHolder: ProgramViewHolder,
        nextItemPosition: Int
    ) {
        val program = workouts[nextItemPosition].workout.program
        subheaderHolder.apply {
            val isSectionExpanded = isSectionExpanded(getSectionIndex(adapterPosition))
            binding.apply {
                programNameText.text = program

                if (isSectionExpanded) {
                    arrow.setImageResource(R.drawable.ic_arrow_up)
                } else {
                    arrow.setImageResource(R.drawable.ic_arrow_down)
                }
            }
            itemView.setOnClickListener {
                headerListener?.invoke(subheaderHolder.adapterPosition)
                Log.d("CHECKTEST","nextPos" + nextItemPosition)
            }
        }
    }

    override fun onPlaceSubheaderBetweenItems(position: Int): Boolean {
        return workouts[position].workout.program != workouts[position + 1].workout.program
    }


    override fun getItemSize(): Int {
        return workouts.size
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

    fun setOptionsListener(listener: ((String, SavedWorkout, Int) -> Unit)?){
        this.optionsListener = listener
    }

    fun swapData(newWorkouts: List<SavedWorkout>) {
        this.workouts.clear()
        this.workouts.addAll(newWorkouts)
//        notifyDataChanged()
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