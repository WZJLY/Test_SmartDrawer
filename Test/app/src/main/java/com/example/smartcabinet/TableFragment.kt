package com.example.smartcabinet

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import android.widget.Toast
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.Drawer
import com.example.smartcabinet.util.Reagent
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
        if(getArguments()!=null)
        {if(getArguments().getString("statue")!="add") {

            if (getArguments().getString("statue") == "drawer")
                drawerID = getArguments().getInt("tablenum")
            if (getArguments().getString("statue") == "op")
                drawerID = getArguments().getInt("tablenum_op")
            drawer = dbManager?.getDrawerByDrawerId(drawerID)

            if (drawer != null)
                num = drawer!!.getDrawerSize()
            addNum(num)
        }
            if(getArguments().getString("statue")=="add")
            {
                num =getArguments().getInt("boxnum")
                addNum(num)
            }
        }
    }

    fun addNum(num: Int){
        tableLayout.removeAllViews()
        for(i in 1..num){
            val tableRow = TableRow(context)
            for(j in 1..num){
                val button = Button(context)
                button.isFocusable = false
                button.id = (i-1)*num+j
                button.setBackgroundResource(R.drawable.btn_style)
                button.text = ""
                if(getArguments().getString("touch")=="false") {
                    button.isClickable = false
                    if(scApp?.getTouchtable()==button.id)
                    {
                        button.setBackgroundResource(R.drawable.btn_choose)
                    }
                    else
                        button.setBackgroundResource(R.drawable.btn_style)
                }
                else {
                    val arrListReagent = dbManager?.reagents
                    val sum = arrListReagent!!.size.toInt()
//                    if(sum == 0)
//                    {
//                        Toast.makeText(context.applicationContext, "没有试剂在该抽屉", Toast.LENGTH_SHORT).show()
//                    }
//                    else
//                    {

                        if(sum>0) {
                            for (m in 1..sum) {
                                reagent = arrListReagent?.get(m - 1)
                                if(reagent!!.drawerId.toInt()==drawerID&&reagent!!.reagentPosition.toInt()==button.id)
                                {  button.text = reagent?.reagentName
                                    if(reagent?.getStatus()==1) {
                                        Toast.makeText(context.applicationContext,"1",Toast.LENGTH_SHORT).show()
                                        button.setBackgroundResource(R.drawable.btn_style1)
                                    }
                                    if(reagent?.getStatus()==2)
                                    {
                                        Toast.makeText(context.applicationContext,"2",Toast.LENGTH_SHORT).show()
                                        button.setBackgroundResource(R.drawable.btn_style2)
                                    }

                                }

                            }
                        }
//                    }
                    button.setOnClickListener { view->
                        view.isFocusable = true
                        view.requestFocus()
                        view.requestFocusFromTouch()
                        val row = button.id.toString()
                        Log.d("data","选择了"+row)

                if(dbManager?.getReagentByPos(drawerID.toString(),row)!=null)
                {
                    if(childFragmentManager.findFragmentByTag("Info")==null) {
                        val informationFragment4 = InformationFragment4()
                        val fragmentTrasaction = childFragmentManager.beginTransaction()
                        val arg = Bundle()
                        arg.putString("tablenum",drawerID.toString())
                        arg.putString("pos",row)
                        informationFragment4.arguments = arg
                        fragmentTrasaction.add(R.id.frameLayout, informationFragment4, "Info")
                        fragmentTrasaction.commit()
                    }
                }
                else
                {
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

                button.setTextColor(Color.BLACK)
                button.width = 55
                button.height = 55
                tableRow.addView(button)
            }
            tableLayout.addView(tableRow)
        }
    }


}// Required empty public constructor
