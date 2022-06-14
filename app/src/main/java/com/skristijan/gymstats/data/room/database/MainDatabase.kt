package com.skristijan.gymstats.data.room.database

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.skristijan.gymstats.data.models.Exercise
import com.skristijan.gymstats.data.models.ExerciseSet
import com.skristijan.gymstats.data.models.Workout
import com.skristijan.gymstats.data.room.dao.ExerciseInfoDao
import com.skristijan.gymstats.data.room.dao.PRDao
import com.skristijan.gymstats.data.room.dao.SavedWorkoutDao
import com.skristijan.gymstats.data.room.dao.WorkoutHistoryDao
import com.skristijan.gymstats.data.room.entities.ExerciseInfo
import com.skristijan.gymstats.data.room.entities.PR
import com.skristijan.gymstats.data.room.entities.SavedWorkout
import com.skristijan.gymstats.data.room.entities.WorkoutHistory
import com.skristijan.gymstats.data.room.typeconverters.WorkoutTypeConverter
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Database(
    version = 1,
    entities = [ExerciseInfo::class, SavedWorkout::class, WorkoutHistory::class, PR::class],
    exportSchema = false
)
@TypeConverters(WorkoutTypeConverter::class)
abstract class MainDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getInstance(app: Application): MainDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(app).also { INSTANCE = it }
        }

        private fun buildDatabase(app: Application) =
            Room.databaseBuilder(
                app,
                MainDatabase::class.java,
                "gym_stats_db"
            )
                .addCallback(object : Callback() {
                    // Pre-populate the database after onCreate has been called. If you want to prepopulate at opening time then override onOpen function instead of onCreate.
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Do database operations through coroutine or any background thread
                        val handler = CoroutineExceptionHandler { _, exception ->
                            println("Caught during database creation --> $exception")
                        }

                        CoroutineScope(Dispatchers.IO).launch(handler) {
                            prepopulateDatabase(
                                getInstance(app).getExerciseInfoDao(),
                                getInstance(app).getSavedWorkoutDao(),
                                getInstance(app).getWorkoutHistoryDao(),
                                getInstance(app).getPRDao()
                            )
                        }
                    }
                })
                .build()

        private suspend fun prepopulateDatabase(
            exerciseDao: ExerciseInfoDao,
            savedWorkoutDao: SavedWorkoutDao,
            workoutHistoryDao: WorkoutHistoryDao,
            prDao: PRDao
        ) {
            prepopulateExerciseDatabase(exerciseDao)
            prepopulateSavedWorkoutsDatabase(savedWorkoutDao)
            prepopulateWorkoutHistoryDatabase(workoutHistoryDao)
            //Test for PRs remove later
            prepopulatePRDatabase(prDao)
        }

        private suspend fun prepopulateExerciseDatabase(dao: ExerciseInfoDao) {

            Log.d("AppDebug", "THIS IS CALLED")

            val test = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do " +
                    "eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
                    "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                    "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat " +
                    "nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

            dao.insert(ExerciseInfo(0, "Barbell Bench Press", "Chest", "Barbell", null, true, test))
            dao.insert(
                ExerciseInfo(
                    0,
                    "Dumbbell Bench Press",
                    "Chest",
                    "Dumbbell",
                    null,
                    true,
                    test
                )
            )
            dao.insert(
                ExerciseInfo(
                    0,
                    "Barbell Incline Bench Press",
                    "Chest",
                    "Barbell",
                    null,
                    true,
                    test
                )
            )
            dao.insert(
                ExerciseInfo(
                    0,
                    "Dumbbell Incline Bench Press",
                    "Chest",
                    "Dumbbell",
                    null,
                    true,
                    test
                )
            )
            dao.insert(ExerciseInfo(0, "Dips", "Chest", "Bodyweight", null, true, test))
            dao.insert(ExerciseInfo(0, "Squat", "Legs", "Barbell", null, true, test))
            dao.insert(ExerciseInfo(0, "Leg Press", "Legs", "Machine", null, true, test))
        }

        private suspend fun prepopulateSavedWorkoutsDatabase(dao: SavedWorkoutDao) {
            val set = ExerciseSet(10, 10, 'n')
            val sets = mutableListOf(set, set, set, set)
            val exercise = Exercise("sit ups", "core", "weighted bodyweight", sets, mutableListOf())
            val exercises =
                mutableListOf(exercise, exercise, exercise, exercise, exercise, exercise, exercise, exercise, exercise, exercise, exercise)
            val workout = Workout("program", "name", mutableListOf(), exercises)
            val test = SavedWorkout(0, workout)

            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)

        }

        private suspend fun prepopulateWorkoutHistoryDatabase(dao: WorkoutHistoryDao) {
            val set = ExerciseSet(10, 10, 'n')
            val sets  = mutableListOf(set, set, set, set, set, set, set, set, set)
            val exercises = mutableListOf(Exercise("sit ups", "core", "weighted bodyweight", sets, mutableListOf()))
            val workout = Workout("program", "name", mutableListOf(), exercises )
            val test = WorkoutHistory(0,"test", 1000L, workout)

            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)
            dao.insert(test)

        }

        private suspend fun prepopulatePRDatabase(dao: PRDao) {
            dao.insert(PR(0, "weight", 90f, 1))
            dao.insert(PR(0, "weight", 100f, 2))
            dao.insert(PR(0, "weight", 105f, 3))
            dao.insert(PR(0, "weight", 107f, 4))
            dao.insert(PR(0, "weight", 110f, 5))
            dao.insert(PR(0, "weight", 98f, 6))
            dao.insert(PR(0, "weight", 100f, 7))
            dao.insert(PR(0, "test", 100f, 7))
        }
    }

    abstract fun getSavedWorkoutDao(): SavedWorkoutDao
    abstract fun getWorkoutHistoryDao(): WorkoutHistoryDao
    abstract fun getExerciseInfoDao(): ExerciseInfoDao
    abstract fun getPRDao(): PRDao
}