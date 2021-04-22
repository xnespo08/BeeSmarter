package com.example.beesmarter.activities.maps

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.beesmarter.IntentConstants
import com.example.beesmarter.R
import com.example.beesmarter.activities.creation.AddEditHiveAreaActivity
import com.example.beesmarter.models.HiveArea
import com.example.beesmarter.viewmodels.creation_models.AddEditHiveAreaViewModel
import com.example.beesmarter.viewmodels.maps.HiveAreaMapViewModel
import com.example.mvvmlibrary.BaseMVVMActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.gson.annotations.SerializedName
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.*
import org.w3c.dom.Text
import java.io.IOException

class HiveAreaMapActivity: BaseMVVMActivity<HiveAreaMapViewModel>(HiveAreaMapViewModel::class.java), CoroutineScope,
    OnMapReadyCallback {
    companion object {
        fun createIntent(context: Context, id: Long?, area: String, latitude: Double, longitude: Double): Intent {
            val intent = Intent(context, HiveAreaMapActivity::class.java)
            id?.let {
                intent.putExtra(IntentConstants.ID, id)
                intent.putExtra(IntentConstants.AREA, area)
                intent.putExtra(IntentConstants.LATITUDE, latitude)
                intent.putExtra(IntentConstants.LONGITUDE, longitude)
            }

            return intent
        }
    }

    override val layout: Int
        get() = R.layout.activity_hive_area_map

    private var id: Long? = null
    private lateinit var hiveArea: HiveArea
    private var areaPlace: String? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var temp: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra(IntentConstants.ID)) {
            id = intent.getLongExtra(IntentConstants.ID, -1)
        }

        if (intent.hasExtra(IntentConstants.AREA)) {
            areaPlace = intent.getStringExtra(IntentConstants.AREA)
        }

        if (intent.hasExtra(IntentConstants.LATITUDE) and intent.hasExtra(IntentConstants.LONGITUDE)) {
            latitude = intent.getDoubleExtra(IntentConstants.LATITUDE, 50.075514)
            longitude = intent.getDoubleExtra(IntentConstants.LONGITUDE, 14.4378)
        }

        if (areaPlace != null) {
            val area = areaPlace
            fetchJson(area).observe(this, Observer {
                val tvMapArea = findViewById<TextView>(R.id.tv_map_area)
                if (it != null) {
                    tvMapArea.text = it.toString()
                }
            })
//            val tvMapArea = findViewById<TextView>(R.id.tv_map_area)
//            tvMapArea.text = temp.toString()
        }

        id?.let {
            launch {
                hiveArea = viewModel.findById(it)
            }
        }

        val btnAddMap = findViewById<Button>(R.id.btn_add_map)
        btnAddMap.setOnClickListener {
            saveHiveArea()
        }

        supportActionBar?.hide()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap?) {
        p0?.apply {
            val area = LatLng(latitude, longitude)
            addMarker(
                MarkerOptions()
                    .position(area)
                    .title("Marker in $area")
            )

            addCircle(
                CircleOptions()
                    .center(area)
                    .radius(3000.0)
                    .strokeColor(Color.RED)
            )
            animateCamera(CameraUpdateFactory.newLatLngZoom(area, 12f))
        }

        p0?.setOnMapClickListener(object: GoogleMap.OnMapClickListener {
            override fun onMapClick(p1: LatLng?) {
                val marker = MarkerOptions()
                if (p1 != null) {
                    marker.position(p1)
                    p0.clear()
                    p0.animateCamera(CameraUpdateFactory.newLatLngZoom(p1, 12f))
                    p0.addMarker(marker)
                    latitude = p1.latitude
                    longitude = p1.longitude
                }

                val circle = CircleOptions()
                if (p1 != null) {
                    circle.center(p1)
                    circle.radius(3000.0)
                    circle.strokeColor(Color.RED)
                    p0.addCircle(circle)
                }
            }
        })
    }

    private fun fetchJson(area: String?) : LiveData<Double> {
        val liveData = MutableLiveData<Double>()
        println("Attempting to Fetch JSON")
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$area&units=metric&appid=0012c8270d67a45d7ac1d0912b539316"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                println(body)

                val gson = GsonBuilder().create()
                val weatherData = gson.fromJson(body, WeatherData::class.java)
                liveData.postValue(weatherData.main.temp)
            }
        })
        return liveData
    }

    inner class WeatherData(val coord: Coord, val weather: List<Weather>, val base: String, val main: TemperatureData,
                            val visibility: Int, val wind: Wind, val clouds: Clouds, val dt: Int, val sys: Sys, val id: Int, val name: String,
                            val cod: Int)

    inner class Sys(
            @SerializedName("type") val type: Int,
            @SerializedName("id") val id: Int,
            @SerializedName("message") val message: Double,
            @SerializedName("country") val country: String,
            @SerializedName("sunrise") val sunrise: Int,
            @SerializedName("sunset") val sunset: Int
    )

    inner class Coord(val lon: Double, val lat: Double)

    inner class TemperatureData(
            @SerializedName("temp") val temp: Double,
            @SerializedName("pressure") val pressure: Int,
            @SerializedName("humidity") val humidity: Int,
            @SerializedName("temp_min") val tempMin: Double,
            @SerializedName("temp_max") val tempMax: Double
    )

    inner class Weather(
            @SerializedName("id") val id: Int,
            @SerializedName("main") val main: String,
            @SerializedName("description") val description: String,
            @SerializedName("icon") val icon: String
    )

    inner class Clouds(
            @SerializedName("all") val all: Int
    )

    inner class Wind(
            @SerializedName("speed") val speed: Double,
            @SerializedName("deg") val deg: Int
    )


    private fun saveHiveArea() {
        hiveArea.latitude = latitude
        hiveArea.longitude = longitude
        id?.let {
            launch {
                viewModel.update(hiveArea)
            }.invokeOnCompletion {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }
}