package com.example.smartcabinet


import android.content.Context
import android.os.Bundle
import android.renderscript.Sampler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.fragment_information2.*
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.ReagentTemplate
import android.widget.ArrayAdapter




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
        val data_list = ArrayAdapter<String>(context, R.layout.information_spinner_style)
        val arrListReagentTemplate = dbManager?.reagentTemplate
        val sum = arrListReagentTemplate!!.size
        if(sum == 0)
        {
            Toast.makeText(context.applicationContext, "当前没有试剂模板", Toast.LENGTH_SHORT).show()
        }
        else
        {

            if(sum>0) {
                for (i in 1..sum) {
                    reagentTemplate = arrListReagentTemplate?.get(i - 1)
                    data_list.add(reagentTemplate!!.reagentName+","+"纯度："+reagentTemplate!!.reagentPurity+"%"
                            +","+reagentTemplate!!.reagentCreater+","+reagentTemplate!!.reagentSize+reagentTemplate!!.reagentUnit)

                }
            }
        }
        data_list?.setDropDownViewResource(R.layout.information_dropdown_style)  //下拉框通过遍历试剂模板试剂库添加源
        spinner_type.adapter=data_list
        spinner_type.setSelection(0)
        spinner_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View,
                                        pos: Int, id: Long) {
                scApp?.templateNum= pos

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
       if(arguments.getString("scan_value")!==null)
       {
           val value = arguments.getString("scan_value")
           eT_code.setText(value)
       }
        btn_code.setOnClickListener{
            scanbuttononClicked("scan")
        }
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
