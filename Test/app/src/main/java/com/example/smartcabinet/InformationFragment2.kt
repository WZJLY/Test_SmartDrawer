package com.example.smartcabinet


import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.fragment_information2.*
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.ReagentTemplate
import android.widget.ArrayAdapter
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class InformationFragment2 : Fragment() {
    var activityCallback:InformationFragment2.scanbuttonlisten? = null
    private var dbManager: DBManager? = null
    private var reagentTemplate:ReagentTemplate?=null
    private var scApp: SCApp? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_information2, container, false)

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        scApp = context.applicationContext as SCApp
        dbManager = DBManager(context.applicationContext)
        val value = arguments.getString("scan_value")
        val weight = arguments.getString("weight")
        val data_list = ArrayAdapter<String>(context, R.layout.information_spinner_style)
        val arrListReagentTemplate = dbManager?.reagentTemplate
        val sum = arrListReagentTemplate!!.size
        initDatePicker()
        if(sum == 0)
        {
            Toast.makeText(context.applicationContext, "当前没有试剂模板", Toast.LENGTH_SHORT).show()
        }
        else
        {

            if(sum>0) {
                for (i in 1..sum) {
                    reagentTemplate = arrListReagentTemplate[i - 1]
                    data_list.add(reagentTemplate!!.reagentName+","+"纯度："+reagentTemplate!!.reagentPurity+"%"
                            +","+reagentTemplate!!.reagentCreater+","+reagentTemplate!!.reagentSize+reagentTemplate!!.reagentUnit)

                }
            }
        }
        data_list.setDropDownViewResource(R.layout.information_dropdown_style)  //下拉框通过遍历试剂模板试剂库添加源
        spinner_type.adapter=data_list
        spinner_type.setSelection(0)
        spinner_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View,
                                        pos: Int, id: Long) {
                scApp?.templateNum= pos
                tV_ml.text = dbManager!!.reagentTemplate.get(pos).reagentUnit
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        if(value!==null) {
            eT_code2.setText(value)
            if(weight !== null){

                eT_weight2.setText(weight)
                eT_residue.isFocusable = true
                eT_residue.isFocusableInTouchMode = true
                eT_residue.requestFocus()
            }
            else{
                eT_weight2.isFocusable = true
                eT_weight2.isFocusableInTouchMode = true
                eT_weight2.requestFocus()
            }
        }
        else{
            if(weight !== null) {
                eT_weight2.setText(weight)
            }
        }
        btn_code2.setOnClickListener{
            scanbuttononClicked("scan")
        }

        eT_data.setOnClickListener {
            val cal = Calendar.getInstance()
            val dialog = DatePickerDialog(context, null, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))

            dialog.setButton(DialogInterface.BUTTON_POSITIVE,"确认", DialogInterface.OnClickListener { dialogInterface, i ->
                val datePicker = dialog.datePicker
                val year = datePicker.year
                val month = datePicker.month+1
                val day = datePicker.dayOfMonth
                eT_data.setText(""+year+"年"+month+"月"+day+"日")
            })
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE,"取消", DialogInterface.OnClickListener { dialogInterface, i ->
                Log.d("setDate","取消")
            })
            dialog.window.setGravity(Gravity.CENTER)
            dialog.datePicker.calendarViewShown = false
            dialog.show()
        }
    }

    fun initDatePicker() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)+1
        val month = cal.get(Calendar.MONTH)+1
        val day  = cal.get(Calendar.DAY_OF_MONTH)
        eT_data.setText(""+year+"年"+month+"月"+day+"日")
    }
    interface scanbuttonlisten {
        fun scanbuttononClick(text: String)
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as scanbuttonlisten
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }

    }
    private fun scanbuttononClicked(text: String) {
        activityCallback?.scanbuttononClick(text)
    }

}// Required empty public constructor
