package com.skristijan.gymstats.ui.chooseExercise

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.skristijan.gymstats.R
import com.skristijan.gymstats.databinding.ActivityChooseExerciseBinding
import com.skristijan.gymstats.ui.fragments.exercises.ExerciseAdapter
import com.skristijan.gymstats.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseExerciseActivity: AppCompatActivity() {

    private val viewModel: ChooseExerciseActivityViewModel by viewModels()

    lateinit var binding: ActivityChooseExerciseBinding
    lateinit var adapter: ChooseExerciseAdapter

    private val result : ArrayList<Int> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
    }

    private fun initComponents(){
        adapter = ChooseExerciseAdapter(mutableListOf())
        binding.exercisesRV.adapter = adapter

        binding.addExToCurWorkout.visibility = View.GONE

        viewModel.getExercises().observe(this, Observer {
            adapter.swapData(it)
        })

        adapter.setOnExerciseTapListener {
            if(it.isEmpty()){
                binding.addExToCurWorkout.visibility = View.GONE
            }else{
                binding.addExToCurWorkout.visibility = View.VISIBLE
            }
            result.clear()
            result.addAll(it)
        }

        binding.addExToCurWorkout.setOnClickListener {
            intent.putIntegerArrayListExtra("exerciseIds", result)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}