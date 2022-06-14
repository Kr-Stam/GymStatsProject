package com.skristijan.gymstats.data.room.typeconverters

import androidx.room.TypeConverter
import com.skristijan.gymstats.data.models.Workout
import com.google.gson.Gson

class WorkoutTypeConverter {

    companion object{
        @TypeConverter
        @JvmStatic
        fun fromWorkout(workout: Workout): String{
            return Gson().toJson(workout)
        }

        @TypeConverter
        @JvmStatic
        fun toWorkout(string: String): Workout {
            return Gson().fromJson(string, Workout::class.java)
        }
    }
}