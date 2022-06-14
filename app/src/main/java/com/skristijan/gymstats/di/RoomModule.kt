package com.skristijan.gymstats.di

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.skristijan.gymstats.data.room.dao.ExerciseInfoDao
import com.skristijan.gymstats.data.room.dao.PRDao
import com.skristijan.gymstats.data.room.dao.SavedWorkoutDao
import com.skristijan.gymstats.data.room.dao.WorkoutHistoryDao
import com.skristijan.gymstats.data.room.database.MainDatabase
import com.skristijan.gymstats.data.room.entities.ExerciseInfo
import com.skristijan.gymstats.repository.SavedWorkoutRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): MainDatabase {
        return MainDatabase.getInstance(app)
    }

    @Provides
    @Singleton
    fun provideExerciseInfoDao(database: MainDatabase): ExerciseInfoDao {
        return database.getExerciseInfoDao()
    }

    @Provides
    @Singleton
    fun provideWorkoutHistoryDao(database: MainDatabase): WorkoutHistoryDao {
        return database.getWorkoutHistoryDao()
    }

    @Provides
    @Singleton
    fun provideSavedWorkoutDao(database: MainDatabase): SavedWorkoutDao {
        return database.getSavedWorkoutDao()
    }

    @Provides
    @Singleton
    fun providePRDao(database: MainDatabase): PRDao {
        return database.getPRDao()
    }

}