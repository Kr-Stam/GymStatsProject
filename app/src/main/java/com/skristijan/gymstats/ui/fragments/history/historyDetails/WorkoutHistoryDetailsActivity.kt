package com.skristijan.gymstats.ui.fragments.history.historyDetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.skristijan.gymstats.data.models.Workout
import com.skristijan.gymstats.databinding.ActivityHistoryDetailsBinding
import com.skristijan.gymstats.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutHistoryDetailsActivity : AppCompatActivity() {

    private val viewModel: WorkoutHistoryDetailsViewModel by viewModels()

    private var _binding: ActivityHistoryDetailsBinding? = null
    private val binding: ActivityHistoryDetailsBinding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WorkoutHistoryDetailsAdapter
    private var id: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHistoryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
    }

    private fun initComponents() {
        initRecyclerView()
        initStartWorkoutBtn()
    }

    private fun initRecyclerView(){
        recyclerView = binding.exercisesRV
        adapter = WorkoutHistoryDetailsAdapter(mutableListOf())
        recyclerView.adapter = adapter
        if (intent.hasExtra("historyId")) {
            id = intent.getIntExtra("historyId", -1)
            viewModel.getWorkout(id)
                .observe(this, Observer { history ->
                    adapter.swapData(history.workout.exercises)
                    binding.nameText.text = history.workout.name
                    binding.dateText.text = history.date
                    if (history.workout.notes != null)
                        binding.noteText.text = notesToString(history.workout.notes)
                    else
                        binding.noteText.visibility = View.GONE
                })
        } else
            Log.d("AppDebug", "ERROR")
    }

    private fun initStartWorkoutBtn() {
        binding.startWorkoutBtn.setOnClickListener {
            intent.putExtra("workoutId", id)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun notesToString(notes: List<String>): String {
        val sb = StringBuilder()
        for (note in notes) {
            sb.append("$note\n")
        }
        sb.removeSuffix("\n")
        return sb.toString()
    }
}