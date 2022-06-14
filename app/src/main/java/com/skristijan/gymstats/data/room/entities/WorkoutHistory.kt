package com.skristijan.gymstats.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.skristijan.gymstats.data.models.Workout
import com.skristijan.gymstats.data.room.typeconverters.WorkoutTypeConverter

@Entity(tableName = "history")
data class WorkoutHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: String,
    val time: Long,
    @TypeConverters(WorkoutTypeConverter::class)
    val workout: Workout
)
