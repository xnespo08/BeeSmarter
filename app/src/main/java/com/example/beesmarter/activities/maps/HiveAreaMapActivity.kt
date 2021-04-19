package com.example.beesmarter.activities.maps

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.IOException

class HiveAreaMapActivity: BaseMVVMActivity<HiveAreaMapViewModel>(HiveAreaMapViewModel::class.java), CoroutineScope,
    OnMapReadyCallback {
    companion object {
        fun createIntent(context: Context, id: Long?, area: String): Intent {
            val intent = Intent(context, HiveAreaMapActivity::class.java)
            id?.let {
                intent.putExtra(IntentConstants.ID, id)
                intent.putExtra(IntentConstants.AREA, area)
            }

            return intent
        }
    }

    override val layout: Int
        get() = R.layout.activity_hive_area_map

    private var id: Long? = null
    private lateinit var hiveArea: HiveArea
    private var lat: Double = 0.0
    private var lng: Double = 0.0
    private var areaPlace: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra(IntentConstants.ID)) {
            id = intent.getLongExtra(IntentConstants.ID, -1)
        }

        if (intent.hasExtra(IntentConstants.HIVES_COUNT)) {
            areaPlace = intent.getStringExtra(IntentConstants.AREA)
        }

        if (id == null) {
            val manager: FragmentManager = supportFragmentManager
            val ft: FragmentTransaction = manager.beginTransaction()
            val fragment: Fragment? = manager.findFragmentById(R.id.map)
            if (fragment != null) {
                ft.hide(fragment)
            }
            ft.commit()
        }

        if (areaPlace != null) {
            val coder = Geocoder(this)
            var address: MutableList<Address> = mutableListOf()

            Places.initialize(getApplicationContext(), "IzaSyCd1rwr2BIqVN9OgQ5CfnQ-XIndicA8v_4");
            try {
                address = coder.getFromLocationName(areaPlace, 5)
                if (address != null) {
                    val location: Address = address[0];
                    lat = location.latitude
                    lng = location.longitude
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap?) {
        p0?.apply {
            val area = LatLng(lat, lng)
            addMarker(
                    MarkerOptions()
                            .position(area)
                            .title("Marker in $area")
            )
            animateCamera(CameraUpdateFactory.newLatLngZoom(area, 15f))
        }
    }
}