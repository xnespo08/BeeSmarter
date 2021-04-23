package com.example.beesmarter.activities.creation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import com.example.beesmarter.IntentConstants
import com.example.beesmarter.R
import com.example.beesmarter.models.Hive
import com.example.beesmarter.models.HiveArea
import com.example.beesmarter.viewmodels.creation_models.AddEditHiveAreaViewModel
import com.example.beesmarter.viewmodels.creation_models.AddEditHiveViewModel
import com.example.mvvmlibrary.BaseMVVMActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class AddEditHiveActivity : BaseMVVMActivity<AddEditHiveViewModel>(AddEditHiveViewModel::class.java),
    CoroutineScope {

    companion object {
        fun createIntent(context: Context, id: Long?, orderName: String): Intent {
            val intent = Intent(context, AddEditHiveActivity::class.java)
            id?.let {
                intent.putExtra(IntentConstants.ID, id)
                intent.putExtra(IntentConstants.ORDER_NAME, orderName)
            }

            return intent
        }
    }

    override val layout: Int
        get() = R.layout.activity_addedit_hive

    private var id: Long? = null
    private var orderName: String? = null
    private lateinit var hive: Hive

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra(IntentConstants.ID)) {
            id = intent.getLongExtra(IntentConstants.ID, -1)
        }

        if (intent.hasExtra(IntentConstants.ORDER_NAME)) {
            orderName = intent.getStringExtra(IntentConstants.ORDER_NAME)
        }

        val btnAddEditHive = findViewById<Button>(R.id.btn_addedit_hive)
        btnAddEditHive.setOnClickListener {
            saveHive()
        }

        id?.let {
            launch {
                hive = viewModel.findById(it)
            }.invokeOnCompletion {
                runOnUiThread {
                    fillLayout()
                }
            }
        }?:kotlin.run {
            hive = Hive("", "",false, 0)
        }

        supportActionBar?.hide()
        setInteractionListeners()
    }

    private fun setInteractionListeners() {
        val etHiveOrderName = findViewById<EditText>(R.id.et_order_name)
        etHiveOrderName.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                hive.orderName = s.toString().trim()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    private fun fillLayout() {
        hive.orderName.let {
            val etHive = findViewById<EditText>(R.id.et_order_name)
            etHive.setText(it)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun saveHive() {
        val etHiveOrderName = findViewById<EditText>(R.id.et_order_name)

        if (etHiveOrderName.text.trim().isNotEmpty()) {
            id?.let {
                launch {
                    viewModel.update(hive)
                }.invokeOnCompletion {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            } ?: kotlin.run {
                launch {
                    viewModel.insert(hive)
                }.invokeOnCompletion {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        } else {
            etHiveOrderName.error = "Invalid Hive Order Name, string expected."
        }
    }
}