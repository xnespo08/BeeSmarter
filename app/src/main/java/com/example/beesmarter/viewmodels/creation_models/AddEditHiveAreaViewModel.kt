package com.example.beesmarter.viewmodels.creation_models

import android.app.Application
import com.example.beesmarter.models.HiveArea
import com.example.beesmarter.viewmodels.base_models.BaseHiveAreaViewModel

class AddEditHiveAreaViewModel(application: Application) : BaseHiveAreaViewModel(application) {

    suspend fun findById(id: Long): HiveArea {
        return hiveAreasRepository.findById(id)
    }

    suspend fun insert(hiveArea: HiveArea) {
        hiveAreasRepository.insert(hiveArea)
    }

    suspend fun update(hiveArea: HiveArea) {
        hiveAreasRepository.update(hiveArea)
    }

    suspend fun delete(hiveArea: HiveArea) {
        hiveAreasRepository.delete(hiveArea)
    }
}