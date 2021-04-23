package com.example.beesmarter.viewmodels.list_models

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.beesmarter.models.Hive
import com.example.beesmarter.models.HiveArea
import com.example.beesmarter.viewmodels.base_models.BaseHiveAreaViewModel
import com.example.beesmarter.viewmodels.base_models.BaseHiveViewModel

class HiveListViewModel(application: Application) : BaseHiveViewModel(application) {

    fun getAll(): LiveData<MutableList<Hive>> {
        return hiveRepository.getAll()
    }

    suspend fun delete(hive: Hive) {
        hiveRepository.delete(hive)
    }

    suspend fun findById(id: Long): Hive {
        return hiveRepository.findById(id)
    }
}