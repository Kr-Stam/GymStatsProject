package com.skristijan.gymstats.data.models

import com.skristijan.gymstats.data.room.entities.SavedWorkout

data class Program(
    val name: String,
    val workouts: MutableList<SavedWorkout>,
    var isExpanded: Boolean = false
) {
}