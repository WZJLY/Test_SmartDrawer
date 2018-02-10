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
import kotlinx.android.synthetic.main.fragment_table.*

/**
 * A simple [Fragment] subclass.
 */
class TableFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_table, container, false)
    }

    fun addNum(num: Int){
        tableLayout.removeAllViews()
        for(i in 1..num){
            val tableRow = TableRow(context)
            for(j in 1..num){
                val button = Button(context)
                button.isFocusable = false
                button.id = (i-1)*3+j
                button.setOnClickListener { view->
                    view.isFocusable = true
                    view.requestFocus()
                    view.requestFocusFromTouch()
                    val row = button.id.toString()
                    Log.d("data","选择了"+row)
                }
                button.setBackgroundResource(R.drawable.btn_style)
                button.text = ""
                button.setTextColor(Color.BLACK)
                button.width = 55
                button.height = 55
                tableRow.addView(button)
            }
            tableLayout.addView(tableRow)
        }
    }

}// Required empty public constructor
