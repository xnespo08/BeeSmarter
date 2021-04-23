package com.example.beesmarter.database.hives

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.beesmarter.models.Hive

@Dao
interface HiveDao {

    @Query("SELECT * FROM hives ORDER BY orderName ASC")
    fun getAll(): LiveData<MutableList<Hive>>

    @Query("SELECT * FROM hives WHERE id = :id")
    suspend fun findById(id: Long): Hive

    //Hive DML
    @Insert
    suspend fun insert(hive: Hive)

    @Update
    suspend fun update(hive: Hive)

    @Delete
    suspend fun delete(hive: Hive)
}