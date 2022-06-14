package com.skristijan.gymstats.data.models

data class ExerciseSet(
    var weight: Long,
    var reps: Int,
    val type: Char,
    var finished: Boolean = false
)