package com.skristijan.gymstats.ui.fragments.workouts

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skristijan.gymstats.data.room.entities.SavedWorkout
import com.skristijan.gymstats.repository.SavedWorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedWorkoutsViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: SavedWorkoutRepository
) : ViewModel() {

    fun getSavedWorkouts(): LiveData<List<SavedWorkout>> {
        return repository.getWorkouts()
    }

    fun getSavedWorkout(id: Int): LiveData<SavedWorkout> {
        return repository.getWorkout(id)
    }

    fun deleteWorkout(workout: SavedWorkout){
        viewModelScope.launch {
            repository.delete(workout)
        }
    }

    fun updateWorkout(workout: SavedWorkout){
        viewModelScope.launch {
            repository.update(workout)
        }
    }

    fun swapWorkouts(workout1: SavedWorkout, workout2: SavedWorkout){
        viewModelScope.launch {
            val tmp = workout1.id
            val tmpP = workout1.workout.program
            workout1.id = workout2.id
            workout1.workout.program = workout2.workout.program
            workout2.id = tmp
            repository.update(workout1)
            repository.update(workout2)
        }
    }

    fun backupWorkouts(workouts: List<SavedWorkout>){
        viewModelScope.launch {
            repository.deleteAll()
            for(workout in workouts){
                repository.insert(workout)
            }
        }
    }

    fun addWorkout(workout: SavedWorkout){
        viewModelScope.launch {
            repository.insert(workout)
        }
    }
}