package com.example.beesmarter.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "hives")
data class Hive(
        @ColumnInfo(name = "orderName")
        var orderName: String,

        @ColumnInfo(name = "lastVisit")
        var lastVisit: String,

        @ColumnInfo(name = "hasQueen")
        var hasQueen: Boolean,

        @ColumnInfo(name = "politeness")
        var politeness: Int,)
{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}