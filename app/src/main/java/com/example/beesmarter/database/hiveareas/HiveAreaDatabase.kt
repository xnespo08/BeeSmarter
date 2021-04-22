package com.example.beesmarter.database.hiveareas

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.beesmarter.models.HiveArea

@Database(entities = [HiveArea::class],
    version = 3,
    exportSchema = false)
abstract class HiveAreaDatabase : RoomDatabase() {

    abstract fun hiveAreasDao(): HiveAreasDao

    companion object {

        private var INSTANCE: HiveAreaDatabase? = null

        fun getDatabase(context: Context) : HiveAreaDatabase {
            if (INSTANCE == null) {
                synchronized(HiveAreaDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = databaseBuilder(
                            context.applicationContext,
                            HiveAreaDatabase::class.java, "attendance_database"
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