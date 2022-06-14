package com.skristijan.gymstats.repository

import androidx.lifecycle.LiveData
import com.skristijan.gymstats.data.room.dao.PRDao
import com.skristijan.gymstats.data.room.entities.PR
import javax.inject.Inject

class PRRepository
@Inject
constructor(private val dao: PRDao){
    suspend fun insert(pr: PR){
        dao.insert(pr)
    }

    suspend fun update(pr: PR){
        dao.update(pr)
    }

    suspend fun delete(pr: PR){
        dao.delete(pr)
    }

    suspend fun deleteAll(){
        dao.deleteAll()
    }

    fun getAll(): LiveData<List<PR>>{
        return dao.getAllPRs()
    }

    fun getPRByType(type: String): LiveData<List<PR>>{
        return dao.getPRByType(type)
    }

    fun getPRById(id: Int): LiveData<PR>{
        return dao.getPRById(id)
    }
}