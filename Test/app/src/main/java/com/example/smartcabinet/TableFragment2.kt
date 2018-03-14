package com.example.smartcabinet


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.Drawer
import kotlinx.android.synthetic.main.fragment_table.*


/**
 * A simple [Fragment] subclass.
 */
class TableFragment2 : Fragment() {

    private var scApp: SCApp? = null
    var num = 0
    var drawerID = 0
    var dbManager: DBManager? = null
    private var drawer: Drawer?=null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_table2, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        scApp = context.applicationContext as SCApp
        if(arguments!=null)
        {
            dbManager = DBManager(context.applicationContext)
            if(arguments.getString("statue")=="drawer")
                drawerID = arguments.getInt("tablenum")
            if(arguments.getString("statue")=="op")
                drawerID = arguments.getInt("tablenum_op")
            drawer=dbManager?.getDrawerByDrawerId(drawerID)

            if(drawer != null)
                num = drawer!!.getDrawerSize()
            addNum2(num)
        }
    }


    fun addNum2(num: Int){
        tableLayout.removeAllViews()
        for(i in 1..num){
            val tableRow = TableRow(context)
            for(j in 1..num){
                val button = Button(context)
                button.isFocusable = false
                button.id = (i-1)*3+j
                button.isClickable = false
                button.text = ""
                button.setTextColor(Color.BLACK)
                button.width = 80
                button.height = 80
                button.setBackgroundResource(R.drawable.btn_table)
                tableRow.addView(button)
            }
            tableLayout.addView(tableRow)
        }
    }


}// Required empty public constructor
