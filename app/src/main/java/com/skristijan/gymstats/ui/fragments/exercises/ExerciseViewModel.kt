package com.skristijan.gymstats.ui.fragments.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skristijan.gymstats.data.room.entities.ExerciseInfo
import com.skristijan.gymstats.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    fun getAllExercises(): LiveData<List<ExerciseInfo>> {
        return exerciseRepository.getAllExercises()
    }

    fun insertExercise(exercise: ExerciseInfo){
        viewModelScope.launch {
            exerciseRepository.insert(exercise)
        }
    }

}