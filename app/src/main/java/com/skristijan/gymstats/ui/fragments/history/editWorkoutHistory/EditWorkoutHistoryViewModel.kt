package com.skristijan.gymstats.ui.fragments.history.editWorkoutHistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skristijan.gymstats.data.models.Exercise
import com.skristijan.gymstats.data.room.entities.ExerciseInfo
import com.skristijan.gymstats.data.room.entities.WorkoutHistory
import com.skristijan.gymstats.repository.ExerciseRepository
import com.skristijan.gymstats.repository.WorkoutHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class EditWorkoutHistoryViewModel
@Inject
constructor(
    private val historyRepository: WorkoutHistoryRepository,
    private val exerciseRepository: ExerciseRepository,
    private val stateHandle: SavedStateHandle
): ViewModel() {

    fun getWorkout(id: Int): LiveData<WorkoutHistory> {
        return historyRepository.getWorkout(id)
    }

    fun updateWorkout(workout: WorkoutHistory){
        viewModelScope.launch {
            historyRepository.update(workout)
        }
    }

    fun deleteWorkout(workout: WorkoutHistory){
        viewModelScope.launch {
            historyRepository.delete(workout)
        }
    }

    fun getExercise(id: Int): LiveData<ExerciseInfo>{
        return exerciseRepository.getExercise(id)
    }
    fun getExercises(ids: ArrayList<Int>): LiveData<List<ExerciseInfo>>{
        return exerciseRepository.getExercises(ids)
    }
}