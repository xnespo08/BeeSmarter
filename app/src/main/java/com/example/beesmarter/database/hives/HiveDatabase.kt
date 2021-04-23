package com.example.beesmarter.database.hives

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.beesmarter.database.hiveareas.HiveAreasDao
import com.example.beesmarter.models.Hive
import com.example.beesmarter.models.HiveArea

@Database(entities = [Hive::class],
    version = 2,
    exportSchema = false)
abstract class HiveDatabase : RoomDatabase() {

    abstract fun hiveDao(): HiveDao

    companion object {

        private var INSTANCE: HiveDatabase? = null

        fun getDatabase(context: Context) : HiveDatabase {
            if (INSTANCE == null) {
                synchronized(HiveDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = databaseBuilder(
                                context.applicationContext,
                                HiveDatabase::class.java, "attendacne_database"
                        )
                                .fallbackToDestructiveMigration()
                                .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}