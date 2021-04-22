package com.example.beesmarter.activities.creation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.beesmarter.IntentConstants
import com.example.beesmarter.R
import com.example.beesmarter.models.HiveArea
import com.example.beesmarter.viewmodels.creation_models.AddEditHiveAreaViewModel
import com.example.mvvmlibrary.BaseMVVMActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class AddEditHiveAreaActivity : BaseMVVMActivity<AddEditHiveAreaViewModel>(AddEditHiveAreaViewModel::class.java),
        CoroutineScope {
    companion object {
        fun createIntent(context: Context, id: Long?, area: String, hivesCount: Int, latitude: Double, longitude: Double): Intent {
            val intent = Intent(context, AddEditHiveAreaActivity::class.java)
            id?.let {
                intent.putExtra(IntentConstants.ID, id)
                intent.putExtra(IntentConstants.AREA, area)
                intent.putExtra(IntentConstants.HIVES_COUNT, hivesCount)
                intent.putExtra(IntentConstants.LATITUDE, latitude)
                intent.putExtra(IntentConstants.LONGITUDE, longitude)
            }

            return intent
        }
    }

    override val layout: Int
        get() = R.layout.activity_addedit_hive_area

    private var id: Long? = null
    private lateinit var hiveArea: HiveArea
    private var areaPlace: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val btnAddEditHiveArea = findViewById<Button>(R.id.btn_addedit_hive_area)
        btnAddEditHiveArea.setOnClickListener {
            saveHiveArea()
        }

        if (intent.hasExtra(IntentConstants.ID)) {
            id = intent.getLongExtra(IntentConstants.ID, -1)
        }

        if (intent.hasExtra(IntentConstants.AREA)) {
            areaPlace = intent.getStringExtra(IntentConstants.AREA)
        }

        id?.let {
            launch {
                hiveArea = viewModel.findById(it)
            }.invokeOnCompletion {
                runOnUiThread {
                    fillLayout()
                }
            }
        }?:kotlin.run {
            hiveArea = HiveArea("", 0, 50.0755, 14.4378)
        }

        supportActionBar?.hide()
        setInteractionListeners()
    }

    private fun setInteractionListeners() {
        val etHiveArea = findViewById<EditText>(R.id.et_area)
        etHiveArea.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                hiveArea.area = s.toString().trim()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        val etHivesCount = findViewById<EditText>(R.id.et_hives_count)
        etHivesCount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                hiveArea.hives_count = s.toString().trim().toInt()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    private fun fillLayout() {
        hiveArea.area.let {
            val etHiveArea = findViewById<EditText>(R.id.et_area)
            etHiveArea.setText(it)
        }

        hiveArea.hives_count.let {
            val etHiveAreaCount = findViewById<EditText>(R.id.et_hives_count)
            etHiveAreaCount.setText(it.toString())
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_addedit_hive_area -> {
                saveHiveArea()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveHiveArea() {
        val etHiveArea = findViewById<EditText>(R.id.et_area)
        val etHiveArenaCount = findViewById<EditText>(R.id.et_hives_count)

        if (etHiveArea.text.trim().isNotEmpty()) {
            if (etHiveArenaCount.text.trim().isNotEmpty() && etHiveArenaCount.text.toString().trim().toInt() > 0) {
                id?.let {
                    launch {
                        viewModel.update(hiveArea)
                    }.invokeOnCompletion {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                } ?: kotlin.run {
                    launch {
                        viewModel.insert(hiveArea)
                    }.invokeOnCompletion {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
            } else {
                etHiveArenaCount.error = "Invalid Hive Arena Count, positive number expected"
            }
        } else {
            etHiveArea.error = "Invalid Hive Area Name, string expected."
        }
    }
}