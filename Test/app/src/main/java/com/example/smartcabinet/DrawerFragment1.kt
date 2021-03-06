package com.example.smartcabinet


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        drawer_tv_enable.visibility = View.GONE
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
        if (dbManager!!.getDrawerByDrawerId(drawerID).statue == "1") {
            drawer_tv_enable.visibility = View.VISIBLE
            drawer_btn_enable.text = "启用"
        }
        iBt_drawer1.setOnClickListener {
            deletDrawerbuttonClicked("find_out",drawerID)
        }
        drawer_btn_enable.setOnClickListener {
            if (dbManager!!.getDrawerByDrawerId(drawerID).statue == "1"){
                dbManager!!.udpateDrawerStatue(drawerID,1,"0")
                drawer_tv_enable.visibility = View.GONE
                drawer_btn_enable.text = "禁用"//禁用抽屉
            }
            else {
                dbManager!!.udpateDrawerStatue(drawerID,1,"1")
                drawer_tv_enable.visibility = View.VISIBLE
                drawer_btn_enable.text = "启用"//禁用抽屉
            }

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


    private fun deletDrawer()
    {
        val arrListReagent = dbManager?.reagents
        val sum = arrListReagent!!.size
        if(sum>0) {
            for (m in 1..sum) {
                reagent = arrListReagent[m - 1]         //修改
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
