package com.example.beesmarter.viewmodels.creation_models

import android.app.Application
import com.example.beesmarter.models.Hive
import com.example.beesmarter.models.HiveArea
import com.example.beesmarter.viewmodels.base_models.BaseHiveAreaViewModel
import com.example.beesmarter.viewmodels.base_models.BaseHiveViewModel

class AddEditHiveViewModel(application: Application) : BaseHiveViewModel(application) {

    suspend fun findById(id: Long): Hive {
        return hiveRepository.findById(id)
    }

    suspend fun insert(hive: Hive) {
        hiveRepository.insert(hive)
    }

    suspend fun update(hive: Hive) {
        hiveRepository.update(hive)
    }

    suspend fun delete(hive: Hive) {
        hiveRepository.delete(hive)
    }
}