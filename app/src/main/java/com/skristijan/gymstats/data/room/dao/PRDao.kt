package com.skristijan.gymstats.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.skristijan.gymstats.data.room.entities.PR

@Dao
interface PRDao {
    @Insert
    suspend fun insert(pr: PR)

    @Delete
    suspend fun delete(pr: PR)

    @Update
    suspend fun update(pr: PR)

    @Query("DELETE from PRs")
    suspend fun deleteAll()

    @Query("SELECT * FROM PRs WHERE type=:type")
    fun getPRByType(type: String): LiveData<List<PR>>

    @Query("SELECT * FROM PRs WHERE id=:id")
    fun getPRById(id: Int): LiveData<PR>

    @Query("SELECT * FROM PRs")
    fun getAllPRs(): LiveData<List<PR>>
}