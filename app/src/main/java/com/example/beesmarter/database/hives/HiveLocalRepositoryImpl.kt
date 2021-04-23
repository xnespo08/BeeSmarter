package com.example.beesmarter.database.hives

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.beesmarter.models.Hive
import com.example.beesmarter.models.HiveArea

class HiveLocalRepositoryImpl(context: Context): IHiveLocalRepository {
    private val hiveDao: HiveDao = HiveDatabase.getDatabase(context).hiveDao()
    private var liveData: LiveData<MutableList<Hive>> = hiveDao.getAll()

    override fun getAll(): LiveData<MutableList<Hive>> {
        return liveData
    }

    override suspend fun findById(id: Long) : Hive {
        return hiveDao.findById(id)
    }

    override suspend fun insert(hive: Hive) {
        hiveDao.insert(hive)
    }

    override suspend fun update(hive: Hive) {
        hiveDao.update(hive)
    }

    override suspend fun delete(hive: Hive) {
        hiveDao.delete(hive)
    }
}