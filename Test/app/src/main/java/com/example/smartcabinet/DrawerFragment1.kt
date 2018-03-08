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
        if(getArguments()!=null)
        {
            drawerID = getArguments().getInt("drawerID")
        }
        return inflater!!.inflate(R.layout.fragment_drawer1, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager = DBManager(context.applicationContext)
        tV_drawer1.text =("抽屉"+drawerID)
        iBt_drawer1.setOnClickListener {
            deletDrawerbuttonClicked("find_out",drawerID)
        }
        iBt_delete.setOnClickListener{

            deletDrawer()

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

    private fun deletDrawer()
    {
        val arrListReagent = dbManager?.reagents
        val sum = arrListReagent!!.size
        if(sum>0) {
            for (m in 1..sum) {
                reagent = arrListReagent?.get(m - 1)
                if(reagent!!.drawerId.toInt()!=drawerID&&dbManager!!.drawers.size==drawerID)
                {
                    dbManager?.deleteDrawer(drawerID,1)
                    deletDrawerbuttonClicked("delet",0)
            }

            }
        }
        else
        {
            if(dbManager!!.drawers.size==drawerID)
                dbManager?.deleteDrawer(drawerID,1)
            deletDrawerbuttonClicked("delet",0) //如果没有不存在试剂，则也只能从最后一个开始删除抽屉

        }
    }       //通过判断该抽屉里是否有试剂，是否是最后一个抽屉，否则无法删除

}// Required empty public constructor
