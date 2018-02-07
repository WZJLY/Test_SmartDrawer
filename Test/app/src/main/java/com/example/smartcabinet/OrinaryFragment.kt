package com.example.smartcabinet

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.orinary_fragment.*

/**
 * Created by WZJ on 2018/1/31.
 */

class OrinaryFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view = inflater!!.inflate(R.layout.orinary_fragment, container, false)
        return view
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {

        regentsearch?.setOnClickListener {
            val intent  = Intent()
            intent.setClass(context.applicationContext,SerachActivity::class.java)
            startActivity(intent)
        }
        edit_flie?.setOnClickListener{
            val intent = Intent()
            intent.setClass(context.applicationContext,EditMessageActivity::class.java)
            startActivity(intent)
        }
        reagent_operation?.setOnClickListener{

        }
        record_query?.setOnClickListener{

        }
    }

}
