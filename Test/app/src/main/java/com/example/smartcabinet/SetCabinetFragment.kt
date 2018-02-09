package com.example.smartcabinet

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_set_cabinet.*


class SetCabinetFragment : Fragment() {
    var activityCallback: SetCabinetFragment.SetCabinetListener ?= null
    interface SetCabinetListener {
        fun setCabinetClick(fragment: String)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as SetCabinetListener
        }catch (e: ClassCastException) {
            Log.d("data","ERROR")
        }
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val cabinetFragment = CabinetFragment()
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.Layout_cabinet,cabinetFragment)
        fragmentTransaction.commit()
        return inflater!!.inflate(R.layout.fragment_set_cabinet, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        iBt_addCabinet.setOnClickListener {
            val cabinetFragment = CabinetFragment()
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.Layout_cabinet,cabinetFragment)
            fragmentTransaction.commit()

        }

        iBt_addDrawer.setOnClickListener {
//            val drawerFragment1 = DrawerFragment1()
//            val fragmentTransient = fragmentManager.beginTransaction()
//            fragmentTransient.add(R.id.Layout_drawer,drawerFragment1)
//            fragmentTransient.commit()
//            drawerFragment1.changeTextProperties("抽屉2")
            activityCallback?.setCabinetClick("setDrawer")
        }
    }
}// Required empty public constructor
