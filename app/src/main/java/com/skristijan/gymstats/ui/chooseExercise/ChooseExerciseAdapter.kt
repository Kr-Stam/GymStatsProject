package com.skristijan.gymstats.ui.chooseExercise

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.skristijan.gymstats.R
import com.skristijan.gymstats.data.room.entities.ExerciseInfo
import com.skristijan.gymstats.databinding.ItemExerciseInfoBinding
import com.skristijan.gymstats.ui.fragments.exercises.ExerciseAdapter

class ChooseExerciseAdapter(private val exercises: MutableList<ExerciseInfo>) :
    RecyclerView.Adapter<ChooseExerciseAdapter.ExerciseViewHolder>() {

    private val exerciseIds: ArrayList<Int> = arrayListOf()

    private val checkDrawable = TextDrawable.builder()
        .beginConfig()
        .textColor(R.color.design_default_color_primary)
        .endConfig()
        .buildRound("✓", Color.LTGRAY)

    fun swapData(newExercises: List<ExerciseInfo>) {
        this.exercises.clear()
        this.exercises.addAll(newExercises)
        notifyDataSetChanged()
    }

    class ExerciseViewHolder(val binding: ItemExerciseInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder(
            ItemExerciseInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val currentExercise = exercises[position]
        val drawable = TextDrawable.builder()
            .beginConfig()
            .bold()
            .textColor(R.color.design_default_color_primary)
            .endConfig()
            .buildRound(currentExercise.name.first().toString(), Color.LTGRAY)
        holder.binding.apply {
            exerciseNameText.text = currentExercise.name

            if(exerciseIds.contains(currentExercise.id)){
                exerciseImageRV.setImageDrawable(checkDrawable)
                holder.itemView.setBackgroundColor(Color.LTGRAY)
            }else if (currentExercise.presetImage || currentExercise.imagePath == null) {
                exerciseImageRV.setImageDrawable(drawable)
                holder.itemView.setBackgroundColor(Color.WHITE)
            } else {
                exerciseImageRV.setImageURI(currentExercise.imagePath.toUri())
                holder.itemView.setBackgroundColor(Color.WHITE)
            }
        }
        holder.itemView.setOnClickListener {
            if(exerciseIds.contains(currentExercise.id)){
                exerciseIds.remove(currentExercise.id)
                holder.binding.exerciseImageRV.setImageDrawable(drawable)
                notifyItemChanged(position)
            }else {
                exerciseIds.add(currentExercise.id)
                holder.binding.exerciseImageRV.setImageDrawable(checkDrawable)
                notifyItemChanged(position)
            }
            listener?.invoke(exerciseIds)
        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    private var listener: ((ArrayList<Int>) -> Unit)? = null

    fun setOnExerciseTapListener(listener: ((ArrayList<Int>) -> Unit)) {
        this.listener = listener
    }

}