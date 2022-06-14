package com.skristijan.gymstats.ui.fragments.history.editWorkoutHistory

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.skristijan.gymstats.data.models.Exercise
import com.skristijan.gymstats.data.models.ExerciseSet
import com.skristijan.gymstats.data.models.Workout
import com.skristijan.gymstats.data.room.entities.WorkoutHistory
import com.skristijan.gymstats.databinding.ActivityEditWorkoutHistoryBinding
import com.skristijan.gymstats.ui.chooseExercise.ChooseExerciseActivity
import com.skristijan.gymstats.ui.chooseExercise.ReplaceExerciseActivity
import com.skristijan.gymstats.ui.main.currentWorkout.CWExercisesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditWorkoutHistoryActivity : AppCompatActivity() {
    //TODO ADD DATE AND TIME SELECTORS

    private val viewModel: EditWorkoutHistoryViewModel by viewModels()
    private var _binding: ActivityEditWorkoutHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var workout: WorkoutHistory
    private lateinit var adapter: CWExercisesAdapter

    private var final: WorkoutHistory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEditWorkoutHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupWorkout()
        setupBtns()
    }

    private fun setupRecyclerView() {
        adapter = CWExercisesAdapter(mutableListOf(), this)
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
        if(id != -1) {
            viewModel.getWorkout(id).observe(this, Observer {
                workout = it
                binding.nameET.text =
                    Editable.Factory.getInstance().newEditable(workout.workout.name)
                for (exercise in workout.workout.exercises)
                    for (set in exercise.sets)
                        set.finished = true
                adapter.swapData(workout.workout.exercises)
                final = workout
                binding.loadingCircle.visibility = View.GONE
                binding.workoutContents.visibility = View.VISIBLE
                binding.topBarContainer.visibility = View.VISIBLE
            })
        }else{
            Toast.makeText(this, "The chosen workout does not exist", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun setupBtns() {
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
                        final!!.workout.exercises.addAll(adapter.finishWorkout())
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