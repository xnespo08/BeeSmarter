package com.example.beesmarter.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.beesmarter.models.HiveArea

class HiveAreaDiffCallback(
    private val oldHiveAreas: List<HiveArea>,
    private val newHiveAreas: List<HiveArea>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldHiveAreas[oldItemPosition].id == newHiveAreas[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldHiveAreas.size
    }

    override fun getNewListSize(): Int {
        return newHiveAreas.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldHiveAreas[oldItemPosition].area == newHiveAreas[newItemPosition].area
    }
}