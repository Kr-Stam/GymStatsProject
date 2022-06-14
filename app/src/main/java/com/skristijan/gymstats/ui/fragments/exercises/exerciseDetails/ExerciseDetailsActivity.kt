package com.skristijan.gymstats.ui.fragments.exercises.exerciseDetails

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.skristijan.gymstats.data.room.entities.ExerciseInfo
import com.skristijan.gymstats.databinding.ActivityExerciseDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseDetailsActivity : AppCompatActivity() {

    private val viewModel: ExerciseDetailsViewModel by viewModels()

    private lateinit var mBinding: ActivityExerciseDetailsBinding
    private lateinit var exercise: ExerciseInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityExerciseDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initComponents()
    }

    private fun initComponents() {
        if (intent.hasExtra("exerciseId")) {
            viewModel.getExercise(intent.getIntExtra("exerciseId", 1)).observe(this, Observer {
                exercise = it
                mBinding.exerciseDescriptionText.text = exercise.description
                //TODO SETUP IMAGE
            })
        } else
            mBinding.exerciseDescriptionText.text = "ERROR"
    }

}