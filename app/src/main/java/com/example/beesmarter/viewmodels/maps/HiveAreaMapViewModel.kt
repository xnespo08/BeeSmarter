package com.example.beesmarter.viewmodels.maps

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.beesmarter.models.HiveArea
import com.example.beesmarter.viewmodels.base_models.BaseHiveAreaViewModel

class HiveAreaMapViewModel(application: Application) : BaseHiveAreaViewModel(application) {

    fun getAll(): LiveData<MutableList<HiveArea>> {
        return hiveAreasRepository.getAll()
    }
}