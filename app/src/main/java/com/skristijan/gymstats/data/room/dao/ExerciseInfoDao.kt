package com.skristijan.gymstats.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.skristijan.gymstats.data.room.entities.ExerciseInfo

@Dao
interface ExerciseInfoDao {

    @Insert
    suspend fun insert(exercise: ExerciseInfo)

    @Update
    suspend fun update(exercise: ExerciseInfo)

    @Delete
    suspend fun delete(exercise: ExerciseInfo)

    @Query("SELECT * FROM exercises")
    fun getAllExercises(): LiveData<List<ExerciseInfo>>

    @Query("SELECT * FROM exercises where id IN (:ids)")
    fun getExercisesFromList(ids: List<Int>): LiveData<List<ExerciseInfo>>

    @Query("SELECT * FROM exercises where id=:id")
    fun getExercise(id: Int): LiveData<ExerciseInfo>

    @Query("DELETE FROM exercises")
    suspend fun deleteAll()
}