package com.example.beesmarter.activities.lists

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beesmarter.IntentConstants
import com.example.beesmarter.R
import com.example.beesmarter.activities.creation.AddEditHiveActivity
import com.example.beesmarter.activities.creation.AddEditHiveAreaActivity
import com.example.beesmarter.models.Hive
import com.example.beesmarter.models.HiveArea
import com.example.beesmarter.utils.HiveDiffCallback
import com.example.beesmarter.viewmodels.base_models.BaseHiveAreaViewModel
import com.example.beesmarter.viewmodels.list_models.HiveListViewModel
import com.example.mvvmlibrary.BaseMVVMActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HiveListActivity: BaseMVVMActivity<HiveListViewModel>(HiveListViewModel::class.java), CoroutineScope {
    companion object {
        fun createIntent(context: Context, id: Long?, areaName: String): Intent {
            val intent = Intent(context, HiveListActivity::class.java)
            id?.let {
                intent.putExtra(IntentConstants.ID, id)
                intent.putExtra(IntentConstants.AREA, areaName)
            }
            return intent
        }
    }

    override val layout: Int
        get() = R.layout.activity_hive_list

    private var id: Long? = null
    private var areaName: String? = null
    private var hiveList: MutableList<Hive> = mutableListOf()
    private lateinit var hiveAdapter: HiveAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra(IntentConstants.AREA)) {
            areaName = intent.getStringExtra(IntentConstants.AREA)
        }

        if (areaName != null) {
            val tvAreaName = findViewById<TextView>(R.id.tv_hive_area)
            tvAreaName.text = areaName
        }

        val fabAddHiveArea = findViewById<FloatingActionButton>(R.id.fab_add_hives)
        fabAddHiveArea.setOnClickListener {
            startActivity(AddEditHiveActivity.createIntent(this, null, ""))
        }

        layoutManager = LinearLayoutManager(this)
        hiveAdapter = HiveAdapter()

        val hiveRecyclerView = findViewById<RecyclerView>(R.id.rv_hives)
        hiveRecyclerView.layoutManager = layoutManager
        hiveRecyclerView.adapter = hiveAdapter

        supportActionBar?.hide()
        viewModel.getAll().observe(this,
                Observer { t ->
                    t?.let {hiveAdapter.updateList(it)}
                })
    }

    inner class HiveAdapter: RecyclerView.Adapter<HiveAdapter.HiveViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiveAdapter.HiveViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_hive_list, parent, false)

            return HiveViewHolder(view)
        }

        override fun getItemCount(): Int = hiveList.size

        override fun onBindViewHolder(holder: HiveAdapter.HiveViewHolder, position: Int) {
            val hive = hiveList[position]
            holder.orderName.text = hive.orderName

            holder.deleteButton.setOnClickListener {
                launch {
                    val delHive = viewModel.findById(hiveList[holder.adapterPosition].id)
                    viewModel.delete(delHive)
                }
            }

            holder.itemView.setOnClickListener {
                startActivity(
                    AddEditHiveActivity.createIntent(
                            this@HiveListActivity,
                            hiveList[holder.adapterPosition].id,
                            hiveList[holder.adapterPosition].orderName
                    )
                )
            }
        }

        fun updateList(newHive: List<Hive>) {
            val diffResult = DiffUtil.calculateDiff(HiveDiffCallback(this@HiveListActivity.hiveList, newHive))

            this@HiveListActivity.hiveList.clear()
            this@HiveListActivity.hiveList.addAll(newHive)

            diffResult.dispatchUpdatesTo(this)
        }

        inner class HiveViewHolder(view: View): RecyclerView.ViewHolder(view) {
            val orderName: TextView = view.findViewById(R.id.tv_hive_order_name)
            val deleteButton: ImageView = view.findViewById(R.id.iv_delete_hive)
        }
    }

}