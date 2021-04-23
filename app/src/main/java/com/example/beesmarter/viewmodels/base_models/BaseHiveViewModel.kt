package com.example.beesmarter.viewmodels.base_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.beesmarter.database.hiveareas.HiveAreasLocalRepositoryImpl
import com.example.beesmarter.database.hiveareas.IHiveAreasLocalRepository
import com.example.beesmarter.database.hives.HiveLocalRepositoryImpl
import com.example.beesmarter.database.hives.IHiveLocalRepository

abstract class BaseHiveViewModel(application: Application) : AndroidViewModel(application) {
    protected var hiveRepository: IHiveLocalRepository = HiveLocalRepositoryImpl(application)
}