package com.skristijan.gymstats.ui.main

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.skristijan.gymstats.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.skristijan.gymstats.R
import com.skristijan.gymstats.data.models.Exercise
import com.skristijan.gymstats.data.models.ExerciseSet
import com.skristijan.gymstats.data.models.Workout
import com.skristijan.gymstats.ui.chooseExercise.ChooseExerciseActivity
import com.skristijan.gymstats.ui.chooseExercise.ReplaceExerciseActivity
import com.skristijan.gymstats.ui.main.currentWorkout.CWExercisesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var bottomNavigationView: BottomNavigationView


    private lateinit var bs: ConstraintLayout
    private lateinit var bsBehavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var finishBtn: Button
    private lateinit var addExerciseBtn: Button
    private lateinit var topBarContainerBS: ConstraintLayout
    private lateinit var collapseBtn: ImageView

    private lateinit var adapter: CWExercisesAdapter

    private var activeWorkout = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initComponents()
    }

    private fun initComponents() {
        setupNavigation()
        setupCurrentWorkout()
    }

    private fun setupNavigation() {
        bottomNavigationView = mBinding.bottomNavView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
    }

    private fun setupCurrentWorkout() {
        topBarContainerBS = mBinding.topBarContainer
        bs = mBinding.bsHolder

        collapseBtn = mBinding.collapseBV
        finishBtn = mBinding.finishWorkoutButton
        addExerciseBtn = mBinding.addExerciseBtn

        setupRecyclerView()

        bs.visibility = View.GONE
        collapseBtn.visibility = View.GONE
        finishBtn.visibility = View.GONE

        val wrapSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        bottomNavigationView.measure(wrapSpec, wrapSpec)

        val params = topBarContainerBS.layoutParams
        params.height = bottomNavigationView.measuredHeight
        topBarContainerBS.layoutParams = params

        bsBehavior = BottomSheetBehavior.from(bs)
        bsBehavior.peekHeight = bottomNavigationView.measuredHeight * 2
        bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        collapseBtn.visibility = View.GONE
                        finishBtn.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        collapseBtn.visibility = View.VISIBLE
                        finishBtn.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        if (collapseBtn.visibility == View.GONE) {
                            collapseBtn.visibility = View.VISIBLE
                            finishBtn.visibility = View.VISIBLE
                            collapseBtn.alpha = 0f
                            finishBtn.alpha = 0f
                        }
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //mBinding.nameText.text = slideOffset.toString()
                collapseBtn.alpha = slideOffset
                finishBtn.alpha = slideOffset
                bottomNavigationView.translationY =
                    bottomNavigationView.height.toFloat() * slideOffset
            }

        }

        bsBehavior.addBottomSheetCallback(bottomSheetCallback)

        finishBtn.setOnClickListener {
            finishWorkout()
        }

        addExerciseBtn.setOnClickListener {
            chooseExercise()
        }
    }

    private fun setupRecyclerView() {
        adapter = CWExercisesAdapter(mutableListOf(), this)
        mBinding.exercisesRV.adapter = adapter

        adapter.setReplaceExerciseListener { i ->
            replaceExercise(i)
        }
    }

    private fun replaceExercise(position: Int) {
        replacePos = position
        val intent = Intent(this, ReplaceExerciseActivity::class.java)
        replaceExLauncher.launch(intent)
    }

    private fun chooseExercise() {
        val intent = Intent(this, ChooseExerciseActivity::class.java)
        chooseExResultLauncher.launch(intent)
    }

    fun startWorkout(workout: Workout?) {
        if (activeWorkout) {
            val builder = AlertDialog.Builder(this)
            builder.setCancelable(true)
            builder.setTitle("Do you want to start a new workout?")
            builder.setMessage("Starting a new workout will destroy the existing one")
            builder.setPositiveButton(
                "Confirm"
            ) { dialog, _ ->
                initWorkout(workout)
                dialog.dismiss()
            }
            builder.setNegativeButton(
                android.R.string.cancel
            ) { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        } else
            initWorkout(workout)
    }

    private fun initWorkout(workout: Workout?) {
        //TODO MOVE PARTS TO VIEW MODEL
        activeWorkout = true
        bs.visibility = View.VISIBLE
        if (workout == null) {
            viewModel.currentWorkout = Workout(
                "Generic",
                "My Workout",
                null,
                mutableListOf()
            )
            adapter.swapData(viewModel.currentWorkout.exercises)
            mBinding.nameText.text = viewModel.currentWorkout.name
        } else {
            viewModel.currentWorkout = workout
            adapter.swapData(viewModel.currentWorkout.exercises)
            mBinding.nameText.text = viewModel.currentWorkout.name
        }
    }

    private fun finishWorkout() {
        viewModel.currentWorkout.exercises.clear()
        viewModel.currentWorkout.exercises.addAll(adapter.finishWorkout())
        val exercises = adapter.finishWorkout()

        if (exercises.isEmpty()) {
            Toast.makeText(this, "Please check some sets", Toast.LENGTH_SHORT).show()
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setCancelable(true)
            builder.setTitle("Are you finished?")
            builder.setMessage("All sets that are not checked will not be saved in history")
            builder.setPositiveButton(
                "Confirm"
            ) { dialog, _ ->
                mBinding.bsHolder.visibility = View.GONE
                bottomNavigationView.translationY = 0F
//                    viewModel.addToHistory(1000, "")
                activeWorkout = false
                dialog.dismiss()
            }
            builder.setNegativeButton(
                android.R.string.cancel
            ) { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
        Log.d("AppDebug", "${viewModel.currentWorkout.exercises}")
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

    private var replacePos: Int = -1

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