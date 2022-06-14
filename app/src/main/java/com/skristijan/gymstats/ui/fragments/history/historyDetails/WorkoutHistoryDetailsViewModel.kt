package com.skristijan.gymstats.ui.fragments.history.historyDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skristijan.gymstats.data.room.entities.WorkoutHistory
import com.skristijan.gymstats.repository.WorkoutHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutHistoryDetailsViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val historyRepository: WorkoutHistoryRepository
) : ViewModel() {

    fun getWorkout(id: Int): LiveData<WorkoutHistory>{
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
}