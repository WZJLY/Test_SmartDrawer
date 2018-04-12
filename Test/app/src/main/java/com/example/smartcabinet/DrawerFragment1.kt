package com.example.smartcabinet


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.Reagent
import kotlinx.android.synthetic.main.fragment_drawer1.*


/**
 * A simple [Fragment] subclass.
 */
class DrawerFragment1 : Fragment() {
    var activityCallback: DrawerFragment1.deletDrawerFragmentListener?= null
    var drawerID  = 0
    private var dbManager: DBManager? = null
    private var reagent:Reagent?=null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if(arguments!=null)
        {
            drawerID = arguments.getInt("drawerID")
        }
        return inflater!!.inflate(R.layout.fragment_drawer1, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager = DBManager(context.applicationContext)
        tV_drawer1.text =("抽屉"+drawerID)
        val arrListReagent = dbManager?.reagents
        val sum = arrListReagent!!.size
        if(sum>0) {
            for (m in 1..sum) {
                reagent = arrListReagent[m - 1]         //试剂
                if(reagent!!.drawerId.toInt()==drawerID)
                {
                    drawer_btn_modify.isEnabled = false
                    break
                }
            }
        }
        iBt_drawer1.setOnClickListener {
            deletDrawerbuttonClicked("find_out",drawerID)
        }
        drawer_btn_enable.setOnClickListener {
            //禁用抽屉
        }
        drawer_btn_modify.setOnClickListener {
            deletDrawerbuttonClicked("drawer_modify",drawerID)
        }
    }
    interface deletDrawerFragmentListener {
        fun deletDrawerButtonClick(text: String,drawerID: Int)
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as deletDrawerFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }

    }
    private fun deletDrawerbuttonClicked(text: String,drawerID: Int) {
        activityCallback?.deletDrawerButtonClick(text,drawerID)
    }
}// Required empty public constructor
