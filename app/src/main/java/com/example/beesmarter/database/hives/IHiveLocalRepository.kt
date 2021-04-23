package com.example.beesmarter.database.hives

import androidx.lifecycle.LiveData
import com.example.beesmarter.models.Hive

interface IHiveLocalRepository {
    fun getAll(): LiveData<MutableList<Hive>>

    suspend fun findById(id: Long) : Hive
    suspend fun insert(hive: Hive)
    suspend fun update(hive: Hive)
    suspend fun delete(hive: Hive)
}