package com.example.smartcabinet


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_drawer2.*


/**
 * A simple [Fragment] subclass.
 */
class DrawerFragment2 : Fragment() {


    var drawerNum = String()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_drawer1, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        iBt_drawer2.setOnClickListener {

        }
        tV_drawer2.text = drawerNum
    }
    fun changeTextProperties(text: String){
        drawerNum = text
    }
}// Required empty public constructor
