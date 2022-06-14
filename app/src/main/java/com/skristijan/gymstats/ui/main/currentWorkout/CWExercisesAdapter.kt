package com.skristijan.gymstats.ui.main.currentWorkout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.skristijan.gymstats.R
import com.skristijan.gymstats.data.models.Exercise
import com.skristijan.gymstats.data.models.ExerciseSet
import com.skristijan.gymstats.databinding.ItemCurrentWorkoutExerciseBinding

class CWExercisesAdapter(
    private val exercises: MutableList<Exercise>,
    private val mContext: Context
) :
    RecyclerView.Adapter<CWExercisesAdapter.ExerciseViewHolder>() {

    val swipeBackground = ColorDrawable(Color.parseColor("#FF0000"))
    val deleteIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_delete)!!

    class ExerciseViewHolder(val binding: ItemCurrentWorkoutExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder(
            ItemCurrentWorkoutExerciseBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val currentExercise = exercises[position]
        val adapter = CWSetsAdapter(mutableListOf())
        holder.binding.apply {
            currentWorkoutExerciseName.text = currentExercise.name
            currentWorkoutExerciseSets.adapter = adapter
            adapter.swapData(currentExercise.sets)
            addSetBtn.setOnClickListener {
                if (currentExercise.sets.isEmpty())
                    currentExercise.sets.add(ExerciseSet(10, 10, 'w'))
                else
                    currentExercise.sets.add(currentExercise.sets.last().copy(finished = false))

                adapter.addSet(currentExercise.sets.last())
            }
            adapter.setOnCheckedListener { i, exerciseSet ->
                exercises[position].sets[i] = exerciseSet
                currentExercise.sets[i] = exerciseSet
            }
            cWExerciseOptions.setOnClickListener {
                val menu = PopupMenu(it.context, it)
                menu.inflate(R.menu.cw_exercise_options_menu)
                menu.show()
                menu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.addNoteMenu -> {
                            //TODO SETUP NOTE ADAPTER
                            true
                        }
                        R.id.deleteMenu -> {
                            removeExercise(position)
                            true
                        }
                        R.id.replaceMenu -> {
                            replaceExerciseListener?.invoke(position)
                            true
                        }
                        else -> true
                    }
                }
            }
        }
        adapter.setDeleteListener { i: Int ->
            exercises[position].sets.removeAt(i)
        }
        adapter.setUndoListener { i, exerciseSet ->
            exercises[position].sets.add(i, exerciseSet)
        }
        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeSet(viewHolder)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemViewSwiped = viewHolder.itemView

                val iconMargin = (itemViewSwiped.height - deleteIcon.intrinsicHeight) / 2

                swipeBackground.setBounds(
                    itemViewSwiped.right + dX.toInt(),
                    itemViewSwiped.top,
                    itemViewSwiped.right,
                    itemViewSwiped.bottom
                )

                deleteIcon.setBounds(
                    itemViewSwiped.right - iconMargin - deleteIcon.intrinsicWidth,
                    itemViewSwiped.top + iconMargin,
                    itemViewSwiped.right - iconMargin,
                    itemViewSwiped.bottom - iconMargin
                )

                swipeBackground.draw(c)

                c.save()

                if (dX > 0) {
                    c.clipRect(
                        itemViewSwiped.left,
                        itemViewSwiped.top,
                        dX.toInt(),
                        itemViewSwiped.bottom
                    )
                } else {
                    c.clipRect(
                        itemViewSwiped.right + dX.toInt(),
                        itemViewSwiped.top,
                        itemViewSwiped.right,
                        itemViewSwiped.bottom
                    )
                }

                deleteIcon.draw(c)

                c.restore()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(holder.binding.currentWorkoutExerciseSets)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    fun swapData(newExercises: List<Exercise>) {
        this.exercises.clear()
        this.exercises.addAll(newExercises)
        notifyDataSetChanged()
    }

    fun insertExercise(exercise: Exercise) {
        exercises.add(exercise)
        notifyItemInserted(exercises.lastIndex)
    }

    private fun removeExercise(position: Int) {
        exercises.removeAt(position)
        notifyItemRemoved(position)
    }

    fun replaceExercise(exercise: Exercise, position: Int) {
        exercises[position] = exercise
        notifyItemChanged(position)
    }

    private var listener: ((Exercise, Boolean) -> Unit)? = null

    fun setOnSetCheckedListener(listener: ((Exercise, Boolean) -> Unit)?) {
        this.listener = listener
    }

    private var replaceExerciseListener: ((Int) -> Unit)? = null

    fun setReplaceExerciseListener(listener: ((Int) -> Unit)?) {
        this.replaceExerciseListener = listener
    }

    fun finishWorkout(): MutableList<Exercise> {
        val result = mutableListOf<Exercise>()
        val sets: MutableList<ExerciseSet> = mutableListOf()
        for (exercise in exercises) {
            if (exercise.sets.isNotEmpty()) {
                for (set in exercise.sets) {
                    if (set.finished)
                        sets.add(set)
                }
                if (sets.isNotEmpty()) {
                    result.add(exercise.copy(sets = sets.toMutableList()))
                    sets.clear()
                }
            }
        }
        return result
    }
}