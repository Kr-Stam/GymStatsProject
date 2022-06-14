package com.skristijan.gymstats.ui.fragments.exercises.exerciseFilters

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.skristijan.gymstats.R
import com.skristijan.gymstats.databinding.ActivityExerciseFiltersBinding

class ExerciseFiltersActivity : AppCompatActivity() {

    private var _binding: ActivityExerciseFiltersBinding? = null
    private val binding: ActivityExerciseFiltersBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityExerciseFiltersBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}