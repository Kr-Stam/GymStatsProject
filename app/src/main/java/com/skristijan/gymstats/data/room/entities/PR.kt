package com.skristijan.gymstats.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PRs")
class PR(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val type: String,
    val weight: Float,
    val date: Long
) {
}