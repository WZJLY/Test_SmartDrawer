package com.example.smartcabinet

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.admin_fragment.*

@Suppress("UNREACHABLE_CODE")
/**
 * Created by WZJ on 2018/1/31.
 */

class AdminFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.admin_fragment, container, false)

    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        reagent_search?.setOnClickListener {
            val intent  = Intent()
            intent.setClass(context.applicationContext,SerachActivity::class.java)
            startActivity(intent)
        }
    }

}
