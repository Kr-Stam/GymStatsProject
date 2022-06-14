package com.skristijan.gymstats.ui.fragments.workouts.workoutDetails

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.skristijan.gymstats.databinding.ActivityWorkoutDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedWorkoutDetailsActivity: AppCompatActivity() {

    private val viewModel: SavedWorkoutDetailsViewModel by viewModels()

    private var _binding: ActivityWorkoutDetailsBinding? = null
    private val binding: ActivityWorkoutDetailsBinding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SavedWorkoutDetailsAdapter

    private var id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWorkoutDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
    }

    private fun initComponents() {
        setupRecyclerView()
        setupStartWorkoutBtn()
    }

    private fun setupRecyclerView(){
        recyclerView = binding.savedWorkoutDetailsExercises
        adapter = SavedWorkoutDetailsAdapter(mutableListOf())
        recyclerView.adapter = adapter
        if(intent.hasExtra("workoutId")) {
            id = intent.getIntExtra("workoutId",-1)
            viewModel.getWorkout(id).observe(this, Observer {workout ->
                adapter.swapData(workout.workout.exercises)
            })
        } else
            Log.d("CHECKTEST", "Workout does not exist")
    }

    private fun setupStartWorkoutBtn(){
        binding.startWorkoutBtn.setOnClickListener {
            intent.putExtra("workoutId", id)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}