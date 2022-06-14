package com.skristijan.gymstats.data.models

data class Workout(
    var program: String,
    var name: String,
    val notes: List<String>?,
    val exercises: MutableList<Exercise>
) {
}