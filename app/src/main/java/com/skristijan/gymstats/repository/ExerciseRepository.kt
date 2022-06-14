package com.skristijan.gymstats.repository

import androidx.lifecycle.LiveData
import com.skristijan.gymstats.data.room.dao.ExerciseInfoDao
import com.skristijan.gymstats.data.room.entities.ExerciseInfo
import javax.inject.Inject

class ExerciseRepository
@Inject
constructor(private val exerciseInfoDao: ExerciseInfoDao) {
    fun getAllExercises(): LiveData<List<ExerciseInfo>> {
        return exerciseInfoDao.getAllExercises()
    }

    fun getExercise(exerciseInfo: ExerciseInfo): LiveData<ExerciseInfo> {
        return exerciseInfoDao.getExercise(exerciseInfo.id)
    }

    fun getExercise(id: Int): LiveData<ExerciseInfo> {
        return exerciseInfoDao.getExercise(id)
    }

    fun getExercises(ids: List<Int>): LiveData<List<ExerciseInfo>>{
        return exerciseInfoDao.getExercisesFromList(ids)
    }


    suspend fun insert(exerciseInfo: ExerciseInfo) {
        exerciseInfoDao.insert(exerciseInfo)
    }

    suspend fun update(exerciseInfo: ExerciseInfo) {
        exerciseInfoDao.update(exerciseInfo)
    }

    suspend fun delete(exerciseInfo: ExerciseInfo) {
        exerciseInfoDao.delete(exerciseInfo)
    }

    suspend fun deleteAll() {
        exerciseInfoDao.deleteAll()
    }
}