package com.skristijan.gymstats.ui.fragments.workouts.editWorkout

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.skristijan.gymstats.data.models.Exercise
import com.skristijan.gymstats.data.models.ExerciseSet
import com.skristijan.gymstats.data.models.Workout
import com.skristijan.gymstats.data.room.entities.SavedWorkout
import com.skristijan.gymstats.databinding.ActivityEditWorkoutBinding
import com.skristijan.gymstats.ui.chooseExercise.ChooseExerciseActivity
import com.skristijan.gymstats.ui.chooseExercise.ReplaceExerciseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditWorkoutActivity : AppCompatActivity() {

    private val viewModel: EditWorkoutViewModel by viewModels()
    private var _binding: ActivityEditWorkoutBinding? = null
    private val binding get() = _binding!!
    private var workout: SavedWorkout? = null
    private lateinit var final: SavedWorkout
    private lateinit var adapter: EWExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEditWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupWorkout()
        setupBtns()
    }


    private fun setupRecyclerView() {
        adapter = EWExerciseAdapter(mutableListOf(), this)
        binding.exercisesRV.adapter = adapter
        setupExOpt()
        binding.loadingCircle.visibility = View.VISIBLE
        binding.workoutContents.visibility = View.INVISIBLE
        binding.topBarContainer.visibility = View.INVISIBLE
    }

    private fun setupExOpt() {
        adapter.setReplaceExerciseListener { i ->
            replaceExercise(i)
        }
    }

    private fun setupWorkout() {
        val id = intent.getIntExtra("id", -1)

        if (id != -1) {
            //Edit existing workout
            viewModel.getWorkout(id).observe(this, Observer {
                workout = it
                final = workout!!
                binding.nameET.text = Editable.Factory.getInstance().newEditable(final.workout.name)
                for (exercise in final.workout.exercises)
                    for (set in exercise.sets)
                        set.finished = false
                Log.d("CHECKTEST", "workout is " + workout)
                adapter.swapData(final.workout.exercises)
                binding.loadingCircle.visibility = View.GONE
                binding.workoutContents.visibility = View.VISIBLE
                binding.topBarContainer.visibility = View.VISIBLE
            })
        } else {
            //Create new workout
            workout = SavedWorkout(0, Workout("My Program", "My Workout", null, mutableListOf()))
            final = workout!!
            binding.nameET.text = Editable.Factory.getInstance().newEditable(final.workout.name)
            for (exercise in final.workout.exercises)
                for (set in exercise.sets)
                    set.finished = false
            Log.d("CHECKTEST", "workout is " + workout)
            adapter.swapData(final.workout.exercises)
            binding.loadingCircle.visibility = View.GONE
            binding.workoutContents.visibility = View.VISIBLE
            binding.topBarContainer.visibility = View.VISIBLE
        }
    }

    fun setupBtns() {
        binding.cancelWorkoutButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setCancelable(true)
                .setMessage("Are you sure you want to cancel?")
                .setNegativeButton("NO", DialogInterface.OnClickListener { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }).setPositiveButton("YES", DialogInterface.OnClickListener { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    finish()
                })
                .create().show()
        }

        binding.saveWorkoutButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setCancelable(true)
                .setMessage("Are you sure you want to save the changes?")
                .setNegativeButton("NO", DialogInterface.OnClickListener { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }).setPositiveButton("YES", DialogInterface.OnClickListener { dialogInterface, _ ->
                    if (final != null) {
                        dialogInterface.dismiss()
                        final!!.workout.exercises.clear()
                        for (exercise in adapter.finishWorkout())
                            if (exercise.sets.isNotEmpty())
                                final!!.workout.exercises.add((exercise))
                        final!!.workout.name = binding.nameET.text.toString()
                        viewModel.updateWorkout(final!!)
                        finish()
                    } else {
                        Toast.makeText(this, "There was an error", Toast.LENGTH_LONG).show()
                    }
                })
                .create().show()
        }

        binding.addExerciseBtn.setOnClickListener {
            addExercises()
        }
    }

    private fun addExercises() {
        val intent = Intent(this, ChooseExerciseActivity::class.java)
        chooseExResultLauncher.launch(intent)
    }

    private var replacePos = -1

    private fun replaceExercise(position: Int) {
        replacePos = position
        val intent = Intent(this, ReplaceExerciseActivity::class.java)
        replaceExLauncher.launch(intent)
    }

    //TODO ADD LOADING CIRCLE

    private val chooseExResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val ids = data.getIntegerArrayListExtra("exerciseIds")
                    if (ids != null) {
                        viewModel.getExercises(ids).observe(this, {
                            it.forEach { exInfo ->
                                adapter.insertExercise(
                                    Exercise(
                                        exInfo.name,
                                        exInfo.bodyPart,
                                        exInfo.category,
                                        mutableListOf(
                                            ExerciseSet(10, 10, 'w')
                                        ),
                                        mutableListOf()
                                    )
                                )
                            }
                        })
                    }
                }
            }
        }

    private val replaceExLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("CHECKTEST", "THIS IS REACHED")
            if (replacePos != -1 && result != null) {
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    if (data != null) {
                        val id = data.getIntExtra("exerciseId", -1)
                        if (id != -1) {
                            viewModel.getExercise(id).observe(this, { exInfo ->
                                adapter.replaceExercise(
                                    Exercise(
                                        exInfo.name,
                                        exInfo.bodyPart,
                                        exInfo.category,
                                        mutableListOf(
                                            ExerciseSet(10, 10, 'w')
                                        ),
                                        mutableListOf()
                                    ), replacePos
                                )
                            })
                        }
                    }
                }
            }
        }
}