package com.skristijan.gymstats.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class ExerciseInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val bodyPart: String,
    val category: String,
    val imagePath: String?,
    val presetImage: Boolean,
    val description: String
)
