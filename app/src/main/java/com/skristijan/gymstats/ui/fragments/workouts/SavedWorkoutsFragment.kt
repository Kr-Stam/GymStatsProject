package com.skristijan.gymstats.ui.fragments.workouts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.skristijan.gymstats.R
import com.skristijan.gymstats.data.models.Exercise
import com.skristijan.gymstats.data.models.ExerciseSet
import com.skristijan.gymstats.data.models.Program
import com.skristijan.gymstats.data.models.Workout
import com.skristijan.gymstats.data.room.entities.SavedWorkout
import com.skristijan.gymstats.databinding.FragmentSavedWorkoutsBinding
import com.skristijan.gymstats.ui.fragments.workouts.editWorkout.EditWorkoutActivity
import com.skristijan.gymstats.ui.fragments.workouts.workoutDetails.SavedWorkoutDetailsActivity
import com.skristijan.gymstats.ui.main.MainActivity
import com.zhukic.sectionedrecyclerview.SectionedRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class SavedWorkoutsFragment : Fragment(), LifecycleObserver {

    companion object {
        fun newInstance() = SavedWorkoutsFragment()
    }

    private val viewModel: SavedWorkoutsViewModel by viewModels()

    private var _binding: FragmentSavedWorkoutsBinding? = null
    private val binding get() = _binding!!

    private lateinit var workoutRV: RecyclerView
    private lateinit var adapter: SavedWorkoutsAdapter
    private lateinit var intent: Intent


    var sortedSavedWorkouts = mutableListOf<SavedWorkout>()

    private val simpleCallback = DragHelper()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedWorkoutsBinding.inflate(inflater, container, false)

        initComponents()

        return binding.root
    }

    private fun initComponents() {
        initRecyclerView()
        initSearchBar()

        binding.startEmptyWorkoutBtn.setOnClickListener {
            startWorkout(null)
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

    //TODO IMPLEMENT DRAG PROPERLY
    private class DragHelper : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        0
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition


            recyclerView.adapter?.notifyItemMoved(
                viewHolder.adapterPosition,
                target.adapterPosition
            )

            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            TODO("Not yet implemented")
        }
    }

    override fun onPause() {
        super.onPause()
        //Use this to save current saved workout list to viewModel via bulk update probably
        Log.d("CHECKTEST", "onPause() called")
//        viewModel.backupWorkouts(sortedSavedWorkouts)
    }


    private fun initRecyclerView() {
        binding.savedWorkoutsRV.visibility = View.INVISIBLE
        binding.startEmptyWorkoutBtn.visibility = View.INVISIBLE
        binding.loadingCircle.visibility = View.VISIBLE

        workoutRV = binding.savedWorkoutsRV
        adapter = SavedWorkoutsAdapter(mutableListOf())
        workoutRV.adapter = adapter
        val testAdapter = SavedWorkoutsProgramAdapter(mutableListOf())
//        workoutRV.adapter = testAdapter

        viewModel.getSavedWorkouts().observe(viewLifecycleOwner, Observer { rawWorkouts ->
            sortedSavedWorkouts = (rawWorkouts.sortedBy { it.workout.program }).toMutableList()
            var i = 0
            val programs = mutableListOf<Program>()
            var name = sortedSavedWorkouts[0].workout.program
            var currentProgram = Program(name, mutableListOf())
            while (i < sortedSavedWorkouts.size) {
                if (name == sortedSavedWorkouts[i].workout.program) {
                    currentProgram.workouts.add(sortedSavedWorkouts[i])
                } else {
                    programs.add(currentProgram)
                    name = sortedSavedWorkouts[i].workout.program
                    currentProgram = Program(name, mutableListOf())
                    currentProgram.workouts.add(sortedSavedWorkouts[i])
                }
                i++
            }
            programs.add(currentProgram)
            testAdapter.swapData(programs)
            sortedSavedWorkouts.sortBy { it.id }
            sortedSavedWorkouts.sortBy { it.workout.program }
//            adapter.swapData(sortedSavedWorkouts)
            adapter.swapData(sortedSavedWorkouts)
            adapter.notifyDataChanged()
            binding.savedWorkoutsRV.visibility = View.VISIBLE
            binding.startEmptyWorkoutBtn.visibility = View.VISIBLE
            binding.loadingCircle.visibility = View.GONE
        })

        adapter.setOnProgramTapListener {
            if (adapter.isSectionExpanded(it)) {
                adapter.collapseSection(it)
            } else {
                adapter.expandSection(it)
            }
        }

        adapter.setOnSavedWorkoutTapListener {
            intent = Intent(context, SavedWorkoutDetailsActivity::class.java)
            intent.putExtra("workoutId", it.id)
            startWorkoutLauncher.launch(intent)
        }

        adapter.setOptionsListener { s, savedWorkout, position ->
            when (s) {
                "program" -> {
                    //TODO IMPLEMENT
                    true
                }
                "edit" -> {
                    val intent = Intent(context, EditWorkoutActivity::class.java)
                    intent.putExtra("id", savedWorkout.id)
                    startActivity(intent)
                    true
                }
                "delete" -> {
                    viewModel.deleteWorkout(savedWorkout)
                    adapter.notifyItemRemoved(position)
                    true
                }
                "duplicate" -> {
                    val workout = SavedWorkout(
                        0,
                        savedWorkout.workout.copy(name = savedWorkout.workout.name + " Copy")
                    )
                    viewModel.addWorkout(workout)
                    true
                }
                else -> true
            }
        }

        val dragHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPositionA = viewHolder.adapterPosition
                val toPositionA = target.adapterPosition
                val adapterT = recyclerView.adapter as SavedWorkoutsAdapter
                if (adapterT.getItemViewType(fromPositionA) != -1 && adapterT.getItemViewType(
                        toPositionA
                    ) != -1
                ) {
                    val viewHolderT = viewHolder as SavedWorkoutsAdapter.SavedWorkoutViewHolder
                    val targetT = target as SavedWorkoutsAdapter.SavedWorkoutViewHolder
                    val fromPosition = viewHolderT.getPos()
                    val toPosition = targetT.getPos()
                    Log.d("CHECKTEST", "from " + fromPosition)
                    Log.d("CHECKTEST", "to " + toPosition)
                    viewModel.swapWorkouts(
                        sortedSavedWorkouts[fromPosition],
                        sortedSavedWorkouts[toPosition]
                    )
                    recyclerView.adapter?.notifyItemMoved(fromPositionA, toPositionA)
                    return false
                } else
                    return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                if (viewHolder is SavedWorkoutsAdapter.ProgramViewHolder) {
                    return makeMovementFlags(0, 0)
                } else
                    return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0)
            }
        }


        val itemTouchHelper = ItemTouchHelper(dragHelper)
        itemTouchHelper.attachToRecyclerView(workoutRV)
    }

    fun startWorkout(workout: Workout?) {
        (activity as MainActivity).startWorkout(workout)
    }

    private fun initSearchBar() {
        binding.searchBar.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText)
                return true
            }

        })
    }

    private val startWorkoutLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null) {
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    if (data != null) {
                        val id = data.getIntExtra("workoutId", -1)
                        if (id != -1) {
                            viewModel.getSavedWorkout(id).observe(this, { workout ->
                                startWorkout(workout.workout)
                            })
                        }
                    }
                }
            }
        }

    private fun filter(text: String?){
        if(text == null || text==""){
            adapter.swapData(sortedSavedWorkouts)
            adapter.notifyDataChanged()
        }else{
            Log.d("CHECKTEST", "Filter is called" + text.toString())
            adapter.swapData(sortedSavedWorkouts.filter { it.workout.name.contains(text, true) })
            adapter.notifyDataChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.saved_workout_search_menu, menu)
        activity?.onCreateOptionsMenu(menu)
    }
}