package com.example.beesmarter.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@Entity(tableName = "hiveareas")
data class HiveArea(

    @ColumnInfo(name = "area")
    var area: String,

    @ColumnInfo(name = "hives_count")
    var hives_count: Int,)
{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}