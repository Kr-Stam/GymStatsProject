package com.skristijan.gymstats.ui.fragments.workouts.editWorkout

import androidx.lifecycle.*
import com.skristijan.gymstats.data.room.entities.ExerciseInfo
import com.skristijan.gymstats.data.room.entities.SavedWorkout
import com.skristijan.gymstats.repository.ExerciseRepository
import com.skristijan.gymstats.repository.SavedWorkoutRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class EditWorkoutViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val exerciseRepository: ExerciseRepository,
    private val savedWorkoutRepository: SavedWorkoutRepository
): ViewModel(){

    fun getWorkout(id: Int): LiveData<SavedWorkout>{
        return savedWorkoutRepository.getWorkout(id)
    }

    fun updateWorkout(workout: SavedWorkout){
        viewModelScope.launch {
            savedWorkoutRepository.update(workout)
        }
    }

    fun insertWorkout(workout: SavedWorkout){
        viewModelScope.launch {
            savedWorkoutRepository.insert(workout)
        }
    }

    fun getExercise(id: Int): LiveData<ExerciseInfo>{
        return exerciseRepository.getExercise(id)
    }

    fun getExercises(ids: ArrayList<Int>): LiveData<List<ExerciseInfo>>{
        return exerciseRepository.getExercises(ids)
    }
}