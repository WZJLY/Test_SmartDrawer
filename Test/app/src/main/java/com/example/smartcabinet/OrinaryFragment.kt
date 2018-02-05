package com.example.smartcabinet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.admin_fragment.*

/**
 * Created by WZJ on 2018/1/31.
 */

class OrinaryFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view = inflater!!.inflate(R.layout.orinary_fragment, container, false)
        return view
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {

        reagent_search?.setOnClickListener {
            val intent  = Intent()
            intent.setClass(context.applicationContext,SerachActivity::class.java)
            startActivity(intent)
        }
    }

}
