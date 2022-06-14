package com.skristijan.gymstats.ui.fragments.exercises

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.skristijan.gymstats.databinding.FragmentExerciseBinding
import com.skristijan.gymstats.ui.fragments.exercises.exerciseDetails.ExerciseDetailsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseFragment : Fragment(), LifecycleObserver {

    companion object {
        fun newInstance() = ExerciseFragment()
    }

    private val viewModel: ExerciseViewModel by viewModels()

    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!

    private lateinit var exerciseRV: RecyclerView
    private lateinit var adapter: ExerciseAdapter
    private lateinit var intent: Intent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseBinding.inflate(inflater, container, false)

        initComponents()

        return binding.root
    }

    private fun initComponents() {
        binding.exercisesRV.visibility = View.INVISIBLE
        binding.loadingCircle.visibility = View.VISIBLE

        exerciseRV = binding.exercisesRV
        adapter = ExerciseAdapter(mutableListOf())
        exerciseRV.adapter = adapter

        viewModel.getAllExercises().observe(viewLifecycleOwner, Observer { exercises ->
            adapter.swapData(exercises)
            binding.exercisesRV.visibility = View.VISIBLE
            binding.loadingCircle.visibility = View.GONE
        })

        adapter.setOnExerciseTapListener {
            Log.d("AppDebug", "Listener works ${it.name}")
            intent = Intent(context, ExerciseDetailsActivity::class.java)
            intent.putExtra("exerciseId", it.id)
            startActivity(intent)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.lifecycle?.addObserver(this)

    }

    //This is for Dialogs and similar objects that could only have been created in onActivityCreated()
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
}