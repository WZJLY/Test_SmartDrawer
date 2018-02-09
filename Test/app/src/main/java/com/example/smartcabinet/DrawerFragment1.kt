package com.example.smartcabinet


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_drawer1.*


/**
 * A simple [Fragment] subclass.
 */
class DrawerFragment1 : Fragment() {

    var drawerID  = 0
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
                if(getArguments()!=null)
                {
                    drawerID = getArguments().getInt("drawerID")
                }
        return inflater!!.inflate(R.layout.fragment_drawer1, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        iBt_drawer1.setOnClickListener {

        }
        tV_drawer1.text =("抽屉"+drawerID)
    }

}// Required empty public constructor
