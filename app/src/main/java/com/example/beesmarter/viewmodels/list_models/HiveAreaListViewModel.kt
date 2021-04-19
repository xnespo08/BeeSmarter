package com.example.beesmarter.viewmodels.list_models

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.beesmarter.models.HiveArea
import com.example.beesmarter.viewmodels.base_models.BaseHiveAreaViewModel

class HiveAreaListViewModel(application: Application) : BaseHiveAreaViewModel(application) {

    fun getAll(): LiveData<MutableList<HiveArea>> {
        return hiveAreasRepository.getAll()
    }

    suspend fun delete(hiveArea: HiveArea) {
        hiveAreasRepository.delete(hiveArea)
    }

    suspend fun findById(id: Long): HiveArea {
        return hiveAreasRepository.findById(id)
    }
}