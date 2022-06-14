package com.skristijan.gymstats.data.models

data class Exercise(
    val name: String,
    val bodyPart: String,
    val category: String,
    val sets: MutableList<ExerciseSet>,
    val notes: MutableList<String>
)
