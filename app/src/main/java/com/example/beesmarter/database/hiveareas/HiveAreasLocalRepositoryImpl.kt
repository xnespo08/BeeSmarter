package com.example.beesmarter.database.hiveareas

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.beesmarter.models.HiveArea

class HiveAreasLocalRepositoryImpl(context: Context) : IHiveAreasLocalRepository {
    private val hiveAreasDao: HiveAreasDao = HiveAreaDatabase.getDatabase(context).hiveAreasDao()
    private var liveData: LiveData<MutableList<HiveArea>> = hiveAreasDao.getAll()

    override fun getAll(): LiveData<MutableList<HiveArea>> {
        return liveData
    }

    override suspend fun findById(id: Long) : HiveArea {
        return hiveAreasDao.findById(id)
    }

    override suspend fun insert(hiveArea: HiveArea) {
        hiveAreasDao.insert(hiveArea)
    }

    override suspend fun update(hiveArea: HiveArea) {
        hiveAreasDao.update(hiveArea)
    }

    override suspend fun delete(hiveArea: HiveArea) {
        hiveAreasDao.delete(hiveArea)
    }
}