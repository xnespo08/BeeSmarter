package com.example.beesmarter.database.hiveareas

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.beesmarter.models.HiveArea

@Dao
interface HiveAreasDao {

    @Query("SELECT * FROM hiveareas ORDER BY area ASC")
    fun getAll(): LiveData<MutableList<HiveArea>>

    @Query("SELECT * FROM hiveareas WHERE id = :id")
    suspend fun findById(id: Long): HiveArea

    //Hive Area DML
    @Insert
    suspend fun insert(hiveArea: HiveArea)

    @Update
    suspend fun update(hiveArea: HiveArea)

    @Delete
    suspend fun delete(hiveArea: HiveArea)

}