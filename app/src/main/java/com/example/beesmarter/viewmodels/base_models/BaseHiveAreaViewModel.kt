package com.example.beesmarter.viewmodels.base_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.beesmarter.database.hiveareas.HiveAreasLocalRepositoryImpl
import com.example.beesmarter.database.hiveareas.IHiveAreasLocalRepository

abstract class BaseHiveAreaViewModel(application: Application) : AndroidViewModel(application) {
    protected var hiveAreasRepository: IHiveAreasLocalRepository = HiveAreasLocalRepositoryImpl(application)
}