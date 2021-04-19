package com.example.beesmarter.activities.lists

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beesmarter.IntentConstants
import com.example.beesmarter.R
import com.example.beesmarter.activities.creation.AddEditHiveAreaActivity
import com.example.beesmarter.activities.maps.HiveAreaMapActivity
import com.example.beesmarter.models.HiveArea
import com.example.beesmarter.utils.HiveAreaDiffCallback
import com.example.beesmarter.viewmodels.list_models.HiveAreaListViewModel
import com.example.mvvmlibrary.BaseMVVMActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HiveAreaListActivity : BaseMVVMActivity<HiveAreaListViewModel>(HiveAreaListViewModel::class.java), CoroutineScope {
    companion object {
        fun createIntent(context: Context, id: Long?, area: String, hives_count: Int) : Intent {
            val intent = Intent(context, HiveAreaListActivity::class.java)
            id?.let {
                intent.putExtra(IntentConstants.ID, id)
                intent.putExtra(IntentConstants.AREA, area)
                intent.putExtra(IntentConstants.HIVES_COUNT, hives_count)
            }
            return intent
        }
    }

    override val layout: Int
        get() = R.layout.activity_hive_area_list

    private var id: Long? = null
    private var hiveAreaList: MutableList<HiveArea> = mutableListOf()
    private lateinit var hiveAreaAdapter: HiveAreaAdapter
    private lateinit var layoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fabAddHiveArea = findViewById<FloatingActionButton>(R.id.fab_add_hive_area)
        fabAddHiveArea.setOnClickListener {
            startActivity(AddEditHiveAreaActivity.createIntent(this, null, "", 0))
        }

        hiveAreaAdapter = HiveAreaAdapter()
        layoutManager = LinearLayoutManager(this)

        val hiveAreasRecyclerView = findViewById<RecyclerView>(R.id.rv_hive_areas)
        hiveAreasRecyclerView.layoutManager = layoutManager
        hiveAreasRecyclerView.adapter = hiveAreaAdapter

        supportActionBar?.hide()
        viewModel.getAll().observe(this,
            Observer { t ->
                t?.let {hiveAreaAdapter.updateList(it)}
            })

    }


    inner class HiveAreaAdapter : RecyclerView.Adapter<HiveAreaAdapter.HiveAreaViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiveAreaAdapter.HiveAreaViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_hive_area_list, parent, false)

            return HiveAreaViewHolder(view)
        }

        override fun getItemCount(): Int = hiveAreaList.size

        override fun onBindViewHolder(holder: HiveAreaAdapter.HiveAreaViewHolder, position: Int) {
            val hiveArea = hiveAreaList[position]
            holder.areaName.text = hiveArea.area
            holder.hiveNumber.text = hiveArea.hives_count.toString()
            holder.deleteButton.setOnClickListener {
                launch {
                    val delHiveArea = viewModel.findById(hiveAreaList[holder.adapterPosition].id!!)
                    viewModel.delete(delHiveArea)
                }
            }
            holder.showMap.setOnClickListener {
                startActivity(
                        HiveAreaMapActivity.createIntent(
                                this@HiveAreaListActivity,
                                hiveAreaList[holder.adapterPosition].id!!,
                                hiveAreaList[holder.adapterPosition].area,
                        )
                )
            }
            holder.itemView.setOnClickListener {
                startActivity(
                    AddEditHiveAreaActivity.createIntent(
                        this@HiveAreaListActivity,
                        hiveAreaList[holder.adapterPosition].id!!,
                        hiveAreaList[holder.adapterPosition].area,
                        hiveAreaList[holder.adapterPosition].hives_count
                    )
                )
            }
        }

        fun updateList(newHiveArea: List<HiveArea>) {
            val diffResult = DiffUtil.calculateDiff(HiveAreaDiffCallback(this@HiveAreaListActivity.hiveAreaList, newHiveArea))

            this@HiveAreaListActivity.hiveAreaList.clear()
            this@HiveAreaListActivity.hiveAreaList.addAll(newHiveArea)

            diffResult.dispatchUpdatesTo(this)
        }

        inner class HiveAreaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val areaName: TextView = view.findViewById(R.id.tv_hive_area_name)
            val hiveNumber: TextView = view.findViewById(R.id.tv_hive_area_number)
            val deleteButton: ImageView = view.findViewById(R.id.iv_delete_hive_area)
            val showMap: ImageView = view.findViewById(R.id.iv_show_map)
        }
    }
}