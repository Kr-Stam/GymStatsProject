package com.skristijan.gymstats.ui.fragments.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skristijan.gymstats.data.room.entities.PR
import com.skristijan.gymstats.repository.PRRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: PRRepository
) : ViewModel() {

    fun insert(pr: PR) {
        viewModelScope.launch {
            repository.insert(pr)
        }
    }

    fun delete(pr: PR) {
        viewModelScope.launch {
            repository.delete(pr)
        }
    }

    fun update(pr: PR) {
        viewModelScope.launch {
            repository.update(pr)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    fun getAllPRs(): LiveData<List<PR>> {
        return repository.getAll()
    }

    fun getPRsByType(type: String): LiveData<List<PR>> {
        return repository.getPRByType(type)
    }

    fun getPRsById(id: Int): LiveData<PR> {
        return repository.getPRById(id)
    }
}