package com.example.smartcabinet

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_advertisement.*

class AdvertisementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advertisement)

        iB_advertisement.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
