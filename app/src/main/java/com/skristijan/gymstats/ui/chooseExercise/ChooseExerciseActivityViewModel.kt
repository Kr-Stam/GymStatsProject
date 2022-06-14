package com.skristijan.gymstats.ui.chooseExercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.skristijan.gymstats.data.room.entities.ExerciseInfo
import com.skristijan.gymstats.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChooseExerciseActivityViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: ExerciseRepository
): ViewModel() {

    fun getExercises(): LiveData<List<ExerciseInfo>>{
        return repository.getAllExercises()
    }
}