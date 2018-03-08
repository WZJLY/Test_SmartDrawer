package com.example.smartcabinet

import android.app.ActivityManager
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import android.widget.Toast
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.Drawer
import com.example.smartcabinet.util.Reagent
import kotlinx.android.synthetic.main.fragment_drawer2.*
import kotlinx.android.synthetic.main.fragment_information4.*
import kotlinx.android.synthetic.main.fragment_table.*



/**
 * A simple [Fragment] subclass.
 */
class TableFragment : Fragment() {

    private var scApp: SCApp? = null
    var num = 0
    var drawerID = 0
    var dbManager: DBManager? = null
    private var drawer:Drawer?=null
    private var reagent:Reagent?=null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_table, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        scApp = context.applicationContext as SCApp
        dbManager = DBManager(context.applicationContext)
        if(arguments!=null)
        {if(arguments.getString("statue")!="add") {
            if (arguments.getString("statue") == "drawer"||arguments.getString("statue")=="drawer1")
                drawerID = arguments.getInt("tablenum")
            if (arguments.getString("statue") == "op")
                drawerID = arguments.getInt("tablenum_op")
            drawer = dbManager?.getDrawerByDrawerId(drawerID)

            if (drawer != null)
                num = drawer!!.getDrawerSize()
            addNum(num)
        }
            if(arguments.getString("statue")=="add")
            {
                num =arguments.getInt("boxnum")
                addNum(num)
            }
        }
    }

    fun addNum(num: Int){
        var buttonStyle:String ?= "noFocusable"
        tableLayout.removeAllViews()
        for(i in 1..num){
            val tableRow = TableRow(context)
            for(j in 1..num){
                val button = Button(context)
                button.isFocusable = false
                button.id = (i-1)*num+j
                button.setBackgroundResource(R.drawable.btn_style)
                button.text = ""


                if(arguments.getString("touch")=="false") {
                    button.isClickable = false
                    if(scApp?.touchtable==button.id)
                    {
                        button.setBackgroundResource(R.drawable.btn_choose)
                    }
                    else
                        button.setBackgroundResource(R.drawable.btn_style)
                }
                else {
                    val arrListReagent = dbManager?.reagents
                    val sum = arrListReagent!!.size
                    if(sum>0) {
                        for (m in 1..sum) {
                            reagent = arrListReagent?.get(m - 1)
                            if(reagent!!.drawerId.toInt()==drawerID&&reagent!!.reagentPosition.toInt()==button.id)
                            {  if(reagent!!.reagentName.length>3)
                                button.text = reagent!!.reagentName.subSequence(0,3)
                                else  button.text = reagent!!.reagentName
                                if(reagent?.status==1) {
                                    Toast.makeText(context.applicationContext,"1",Toast.LENGTH_SHORT).show()
                                    button.setBackgroundResource(R.drawable.btn_style1)
                                }
                                if(reagent?.status==2)
                                {
                                    Toast.makeText(context.applicationContext,"2",Toast.LENGTH_SHORT).show()
                                    button.setBackgroundResource(R.drawable.btn_style2)
                                }

                            }

                        }
                    }
                    button.setOnClickListener { view->
                        view.isFocusable = true
                        view.requestFocus()
                        view.requestFocusFromTouch()
                        val row = button.id.toString()
                        val operationActivity = activity as OperationActivity
                        if(dbManager?.getReagentByPos(drawerID.toString(),row)!=null)
                        {
                            if(dbManager!!.getReagentByPos(drawerID.toString(),row).status==1)
                                operationActivity.changeMessage("style1")
                            else
                                operationActivity.changeMessage("style2")
                            if(childFragmentManager.findFragmentByTag("Info")==null) {
                                val informationFragment4 = InformationFragment4()
                                val fragmentTrasaction = childFragmentManager.beginTransaction()
                                val arg = Bundle()
                                arg.putString("tablenum",drawerID.toString())
                                arg.putString("pos",row)
                                informationFragment4.arguments = arg
                                fragmentTrasaction.replace(R.id.frameLayout, informationFragment4, "Info")
                                fragmentTrasaction.commit()
                            }
                            else
                            {
                                tV_num2.text!=dbManager!!.getReagentByPos(drawerID.toString(),row).reagentId
                                tV_name2.text=dbManager!!.getReagentByPos(drawerID.toString(),row).reagentName
                                tV_purity2.text=dbManager!!.getReagentByPos(drawerID.toString(),row).reagentPurity
                                tV_data2.text=dbManager!!.getReagentByPos(drawerID.toString(),row).reagentInvalidDate
                                tV_manufactor2.text=dbManager!!.getReagentByPos(drawerID.toString(),row).reagentCreater
                                tV_residue2.text=dbManager!!.getReagentByPos(drawerID.toString(),row).reagentSize
                                tV_person2.text=dbManager!!.getReagentByPos(drawerID.toString(),row).reagentUser
                            }
                        }
                        else
                        {
                            operationActivity.changeMessage("style_denglu")
                            if(childFragmentManager.findFragmentByTag("Info")!=null) {
                            val informationFragment4 = childFragmentManager.findFragmentByTag("Info")
                            val fragmentTrasaction = fragmentManager.beginTransaction()

                            fragmentTrasaction.remove(informationFragment4)
                            fragmentTrasaction.commit()
                            }
                        }
                        scApp?.setTouchdrawer(drawerID)
                        scApp?.setTouchtable(button.id)
                    }
                }
                if(getArguments().getString("statue")=="add"||getArguments().getString("statue")=="drawer")
                {
                    button.isClickable = false
                }
                button.setTextColor(Color.BLACK)
                button.width = 55
                button.height = 55
                tableRow.addView(button)
            }
            tableLayout.addView(tableRow)
        }
    }



}// Required empty public constructor
