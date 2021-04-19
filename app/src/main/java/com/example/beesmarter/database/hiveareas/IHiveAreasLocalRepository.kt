package com.example.beesmarter.database.hiveareas

import androidx.lifecycle.LiveData
import com.example.beesmarter.models.HiveArea

interface IHiveAreasLocalRepository {
    fun getAll(): LiveData<MutableList<HiveArea>>

    suspend fun findById(id: Long) : HiveArea
    suspend fun insert(hiveArea: HiveArea)
    suspend fun update(hiveArea: HiveArea)
    suspend fun delete(hiveArea: HiveArea)
}