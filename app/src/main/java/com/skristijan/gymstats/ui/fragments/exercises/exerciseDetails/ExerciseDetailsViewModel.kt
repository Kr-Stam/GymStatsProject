package com.skristijan.gymstats.ui.fragments.exercises.exerciseDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.skristijan.gymstats.data.room.entities.ExerciseInfo
import com.skristijan.gymstats.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExerciseDetailsViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    fun getExercise(id: Int): LiveData<ExerciseInfo>{
        return exerciseRepository.getExercise(id)
    }
}