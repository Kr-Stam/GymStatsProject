package com.skristijan.gymstats.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.skristijan.gymstats.data.models.Workout
import com.skristijan.gymstats.data.room.typeconverters.WorkoutTypeConverter

@Entity(tableName = "workouts")
data class SavedWorkout(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @TypeConverters(WorkoutTypeConverter::class)
    val workout: Workout
)
