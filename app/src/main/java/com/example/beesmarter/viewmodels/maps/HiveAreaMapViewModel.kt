package com.example.beesmarter.viewmodels.maps

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.beesmarter.models.HiveArea
import com.example.beesmarter.viewmodels.base_models.BaseHiveAreaViewModel

class HiveAreaMapViewModel(application: Application) : BaseHiveAreaViewModel(application) {

    fun getAll(): LiveData<MutableList<HiveArea>> {
        return hiveAreasRepository.getAll()
    }

    suspend fun findById(id: Long): HiveArea {
        return hiveAreasRepository.findById(id)
    }

    suspend fun update(hiveArea: HiveArea) {
        hiveAreasRepository.update(hiveArea)
    }
}