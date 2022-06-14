package com.skristijan.gymstats.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.skristijan.gymstats.data.room.entities.SavedWorkout

@Dao
interface SavedWorkoutDao {

    @Insert
    suspend fun insert(workout: SavedWorkout)

    @Update
    suspend fun update(workout: SavedWorkout)

    @Delete
    suspend fun delete(workout: SavedWorkout)

    @Query("SELECT * FROM workouts where id=:id")
    fun getWorkout(id: Int): LiveData<SavedWorkout>

    @Query("SELECT * FROM workouts")
    fun getAllWorkouts(): LiveData<List<SavedWorkout>>

    @Query("DELETE FROM workouts")
    suspend fun deleteAll()
}