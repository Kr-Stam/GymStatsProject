package com.skristijan.gymstats.ui.fragments.workouts.editWorkout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.skristijan.gymstats.R
import com.skristijan.gymstats.data.models.Exercise
import com.skristijan.gymstats.data.models.ExerciseSet
import com.skristijan.gymstats.databinding.ItemEditWorkoutExerciseBinding

class EWExerciseAdapter
    (
    private val exercises: MutableList<Exercise>,
    private val mContext: Context
) :
    RecyclerView.Adapter<EWExerciseAdapter.ExerciseViewHolder>() {

    val swipeBackground = ColorDrawable(Color.parseColor("#FF0000"))
    val deleteIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_delete)!!

    class ExerciseViewHolder(val binding: ItemEditWorkoutExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder(
            ItemEditWorkoutExerciseBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val currentExercise = exercises[position]
        val adapter = EWSetsAdapter(mutableListOf(), mContext)
        holder.binding.apply {
            currentWorkoutExerciseName.text = currentExercise.name
            currentWorkoutExerciseSets.adapter = adapter
            adapter.swapData(currentExercise.sets)
            currentWorkoutExerciseSets.adapter = adapter
            addSetBtn.setOnClickListener {
                if (currentExercise.sets.isEmpty())
                    currentExercise.sets.add(ExerciseSet(10, 10, 'w'))
                else
                    currentExercise.sets.add(currentExercise.sets.last().copy(finished = false))

                adapter.addSet(currentExercise.sets.last())
            }
            //TODO SETUP NOTE ADAPTER
            cWExerciseOptions.setOnClickListener {
                val menu = PopupMenu(it.context, it)
                menu.inflate(R.menu.cw_exercise_options_menu)
                menu.show()
                menu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.addNoteMenu -> {
                            //TODO UPDATE NOTE ADAPTER
                            exercises[position].notes.add("")
                            true
                        }
                        R.id.removeMenu -> {
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
        adapter.setRepListener { i, reps ->
            exercises[position].sets[i].reps = reps
        }
        adapter.setWeightListener { i, weight ->
            exercises[position].sets[i].weight = weight
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
        Log.d("CheckTEST", position.toString())
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
        return exercises
    }
}