package com.example.beesmarter.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.beesmarter.models.Hive

class HiveDiffCallback(
    private val oldHive: List<Hive>,
    private val newHive: List<Hive>) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldHive[oldItemPosition].id == newHive[newItemPosition].id
        }

        override fun getOldListSize(): Int {
            return oldHive.size
        }

        override fun getNewListSize(): Int {
            return newHive.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

            val order = oldHive[oldItemPosition].orderName == newHive[newItemPosition].orderName
            //TODO dodelat
            return order
        }
}