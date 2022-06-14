package com.skristijan.gymstats.repository

import androidx.lifecycle.LiveData
import com.skristijan.gymstats.data.room.dao.SavedWorkoutDao
import com.skristijan.gymstats.data.room.entities.SavedWorkout
import javax.inject.Inject

class SavedWorkoutRepository
@Inject
constructor(private val savedWorkoutDao: SavedWorkoutDao) {
    fun getWorkouts(): LiveData<List<SavedWorkout>>{
        return savedWorkoutDao.getAllWorkouts()
    }

    fun getWorkout(workout: SavedWorkout): LiveData<SavedWorkout>{
        return savedWorkoutDao.getWorkout(workout.id)
    }

    fun getWorkout(id: Int): LiveData<SavedWorkout>{
        return savedWorkoutDao.getWorkout(id)
    }

    suspend fun insert(workout: SavedWorkout){
        savedWorkoutDao.insert(workout)
    }

    suspend fun update(workout: SavedWorkout){
        savedWorkoutDao.update(workout)
    }

    suspend fun delete(workout: SavedWorkout){
        savedWorkoutDao.delete(workout)
    }

    suspend fun deleteAll(){
        savedWorkoutDao.deleteAll()
    }
}