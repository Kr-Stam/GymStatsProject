package com.skristijan.gymstats.ui.fragments.history

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
class WorkoutHistoryViewModel
@Inject
constructor(
    private val repository: WorkoutHistoryRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun getWorkoutHistory(): LiveData<List<WorkoutHistory>> {
        return repository.getHistory()
    }

    fun getWorkout(id: Int): LiveData<WorkoutHistory> {
        return repository.getWorkout(id)
    }

    fun deleteAll(){
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    fun deleteHistory(workoutHistory: WorkoutHistory){
        viewModelScope.launch {
            repository.delete(workoutHistory)
        }
    }
}

