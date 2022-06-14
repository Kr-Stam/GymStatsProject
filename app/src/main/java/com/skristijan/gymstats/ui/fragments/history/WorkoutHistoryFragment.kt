package com.skristijan.gymstats.ui.fragments.history

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.skristijan.gymstats.data.models.Workout
import com.skristijan.gymstats.databinding.FragmentWorkoutHistoryBinding
import com.skristijan.gymstats.ui.fragments.history.editWorkoutHistory.EditWorkoutHistoryActivity
import com.skristijan.gymstats.ui.fragments.history.historyDetails.WorkoutHistoryDetailsActivity
import com.skristijan.gymstats.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutHistoryFragment : Fragment(), LifecycleObserver {

    companion object {
        fun newInstance() = WorkoutHistoryFragment()
    }

    private val viewModel: WorkoutHistoryViewModel by viewModels<WorkoutHistoryViewModel>()

    private var _binding: FragmentWorkoutHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var historyRV: RecyclerView
    private lateinit var adapter: WorkoutHistoryAdapter
    private lateinit var intent: Intent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutHistoryBinding.inflate(inflater, container, false)

        initComponents()


        return binding.root
    }

    private fun initComponents() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.historyRV.visibility = View.INVISIBLE
        binding.loadingCircle.visibility = View.VISIBLE

        historyRV = binding.historyRV
        adapter = WorkoutHistoryAdapter(mutableListOf())
        historyRV.adapter = adapter

        viewModel.getWorkoutHistory().observe(viewLifecycleOwner, Observer {
            adapter.swapData(it)
            binding.historyRV.visibility = View.VISIBLE
            binding.loadingCircle.visibility = View.GONE
        })

        adapter.setOnHistoryTapListener {
            intent = Intent(context, WorkoutHistoryDetailsActivity::class.java)
            intent.putExtra("historyId", it.id)
            startWorkoutLauncher.launch(intent)
        }

        adapter.setOptionsListener { s, workoutHistory, i ->
            when(s){
                "delete" -> {
                    viewModel.deleteHistory(workoutHistory)
                    adapter.notifyItemRemoved(i)
                    true
                }
                "edit" -> {
                    val intent = Intent(context, EditWorkoutHistoryActivity::class.java)
                    intent.putExtra("id", workoutHistory.id)
                    startActivity(intent)
                    true
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.lifecycle?.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreateEvent() {
        // Remove the LifecycleObserver once you get a callback to ON_CREATE
        activity?.lifecycle?.removeObserver(this)

        // Then do your logic that specifically needs to wait for the Activity
        // to be created
        Log.d("callback", "lifecycle observer is called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val startWorkoutLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null) {
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    if (data != null) {
                        val id = data.getIntExtra("workoutId", -1)
                        if (id != -1) {
                            viewModel.getWorkout(id).observe(this, { workout ->
                                startWorkout(workout.workout)
                            })
                        }
                    }
                }
            }
        }

    fun startWorkout(workout: Workout?) {
        workout?.exercises?.forEach { exercise ->
            exercise.sets.forEach { set ->
                set.finished = false
            }
        }
        (activity as MainActivity).startWorkout(workout)
    }

}