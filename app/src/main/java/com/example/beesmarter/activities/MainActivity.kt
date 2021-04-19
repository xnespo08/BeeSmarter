package com.example.beesmarter.activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.beesmarter.R
import com.example.beesmarter.activities.lists.HiveAreaListActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_start = findViewById<Button>(R.id.btn_start)

        btn_start.setOnClickListener {
            startActivity(HiveAreaListActivity.createIntent(this, null, "", 0))
        }

    }

//    override fun onResume() {
//        super.onResume()
//        finish()
//    }
}