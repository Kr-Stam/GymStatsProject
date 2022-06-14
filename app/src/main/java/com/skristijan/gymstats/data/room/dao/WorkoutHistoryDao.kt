package com.skristijan.gymstats.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.skristijan.gymstats.data.room.entities.WorkoutHistory

@Dao
interface WorkoutHistoryDao {

    @Insert
    suspend fun insert(workout: WorkoutHistory)

    @Update
    suspend fun update(workout: WorkoutHistory)

    @Delete
    suspend fun delete(workout: WorkoutHistory)

    @Query("SELECT * FROM history where id=:id")
    fun getWorkout(id: Int):LiveData<WorkoutHistory>

    @Query("SELECT * FROM history ORDER BY id DESC")
    fun getAllWorkouts():LiveData<List<WorkoutHistory>>

    @Query("DELETE FROM history")
    suspend fun deleteAll()
}