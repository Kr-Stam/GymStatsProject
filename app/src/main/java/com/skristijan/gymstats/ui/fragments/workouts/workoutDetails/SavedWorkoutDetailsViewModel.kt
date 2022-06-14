package com.skristijan.gymstats.ui.fragments.workouts.workoutDetails

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
class SavedWorkoutDetailsViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: SavedWorkoutRepository
) : ViewModel() {

    fun getWorkout(id: Int): LiveData<SavedWorkout>{
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
}