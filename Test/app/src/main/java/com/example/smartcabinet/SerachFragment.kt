package com.example.smartcabinet

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentContainer
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.admin_fragment.*
import kotlinx.android.synthetic.main.serach_fragment.*

/**
 * Created by WZJ on 2018/1/31.
 */

class SerachFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.serach_fragment, container, false)
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        Toast.makeText(context.applicationContext,"ffffff",Toast.LENGTH_LONG).show()
//
//    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
             button?.setOnClickListener{
                 val fragment = childFragmentManager.beginTransaction()
                 val tfrg = TextFragment()
                 fragment.add(R.id.add_drawer, tfrg, "TAG")
                 fragment.commit()
                 val fragment1 = childFragmentManager.beginTransaction()
                 val tfrg1 = TextFragment()
                 fragment1.add(R.id.add_drawer, tfrg1, "TAG1")
                 fragment1.commit()
             }
        button2?.setOnClickListener{
            val frag = childFragmentManager.findFragmentByTag("TAG")
            val fragment = childFragmentManager.beginTransaction()
            fragment.remove(frag)
            fragment.commit()

        }
 }







}
