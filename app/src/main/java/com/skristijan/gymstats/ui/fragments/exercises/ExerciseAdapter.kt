package com.skristijan.gymstats.ui.fragments.exercises

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.skristijan.gymstats.R
import com.skristijan.gymstats.data.room.entities.ExerciseInfo
import com.skristijan.gymstats.databinding.ItemExerciseInfoBinding

class ExerciseAdapter(private val exercises: MutableList<ExerciseInfo>) :
    RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

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
        holder.binding.apply {
            exerciseNameText.text = currentExercise.name

            if (currentExercise.presetImage || currentExercise.imagePath == null) {
                val drawable = TextDrawable.builder()
                    .beginConfig()
                    .textColor(R.color.design_default_color_primary_dark)
                    .endConfig()
                    .buildRound(currentExercise.name.first().toString(), Color.LTGRAY)

                exerciseImageRV.setImageDrawable(drawable)
            } else {
                exerciseImageRV.setImageURI(currentExercise.imagePath.toUri())
            }
        }
        holder.itemView.setOnClickListener {
            listener?.invoke(currentExercise)
        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    private var listener: ((ExerciseInfo) -> Unit)? = null

    fun setOnExerciseTapListener(listener: ((ExerciseInfo) -> Unit)) {
        this.listener = listener
    }
}