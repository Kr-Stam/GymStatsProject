package com.skristijan.gymstats.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.skristijan.gymstats.data.models.Workout
import com.skristijan.gymstats.data.room.entities.ExerciseInfo
import com.skristijan.gymstats.data.room.entities.WorkoutHistory
import com.skristijan.gymstats.repository.ExerciseRepository
import com.skristijan.gymstats.repository.SavedWorkoutRepository
import com.skristijan.gymstats.repository.WorkoutHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val workoutRepository: SavedWorkoutRepository,
    private val historyRepository: WorkoutHistoryRepository,
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {

    lateinit var currentWorkout: Workout
    var finishedSets: MutableList<MutableList<Boolean>>? = null

    fun addToHistory(time: Long, date: String) {
        viewModelScope.launch {
            historyRepository.insert(WorkoutHistory(0, date, time, currentWorkout))
        }
    }

    fun getExercises(ids: List<Int>): LiveData<List<ExerciseInfo>>{
        return exerciseRepository.getExercises(ids)
    }

    fun getExercise(id: Int): LiveData<ExerciseInfo>{
        return exerciseRepository.getExercise(id)
    }

}