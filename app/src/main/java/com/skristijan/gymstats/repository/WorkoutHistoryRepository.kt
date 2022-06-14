package com.skristijan.gymstats.repository

import androidx.lifecycle.LiveData
import com.skristijan.gymstats.data.room.dao.WorkoutHistoryDao
import com.skristijan.gymstats.data.room.entities.WorkoutHistory
import javax.inject.Inject

class WorkoutHistoryRepository
@Inject
constructor(private val workoutHistoryDao: WorkoutHistoryDao) {
    fun getHistory(): LiveData<List<WorkoutHistory>> {
        return workoutHistoryDao.getAllWorkouts()
    }

    fun getWorkout(workout: WorkoutHistory): LiveData<WorkoutHistory> {
        return workoutHistoryDao.getWorkout(workout.id)
    }

    fun getWorkout(id: Int): LiveData<WorkoutHistory> {
        return workoutHistoryDao.getWorkout(id)
    }

    suspend fun insert(workout: WorkoutHistory) {
        workoutHistoryDao.insert(workout)
    }

    suspend fun update(workout: WorkoutHistory) {
        workoutHistoryDao.update(workout)
    }

    suspend fun delete(workout: WorkoutHistory) {
        workoutHistoryDao.delete(workout)
    }

    suspend fun deleteAll() {
        workoutHistoryDao.deleteAll()
    }
}