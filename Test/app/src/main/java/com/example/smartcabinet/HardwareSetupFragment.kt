package com.example.smartcabinet


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.smartcabinet.util.DBManager
import kotlinx.android.synthetic.main.fragment_hardware_setup.*


/**
 * A simple [Fragment] subclass.
 */
class HardwareSetupFragment : Fragment() {
    private var dbManager: DBManager? = null
    var activityCallback: HardwareSetupFragment.hardwareSetupListener?= null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_hardware_setup, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var serialNumber = 1
        dbManager = DBManager(context)

        if(dbManager!!.cabinetNo.size > 0){
            serialNumber = dbManager!!.cabinetNo[0].serialNumber.toInt()
        }
        val Madapter = ArrayAdapter.createFromResource(context, R.array.serialPort, R.layout.spinner_style)
        Madapter?.setDropDownViewResource(R.layout.dropdown_style)
        sp_serialPort.adapter = Madapter
        sp_serialPort.setSelection(serialNumber-1)
        sp_serialPort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View,
                                        pos: Int, id: Long) {
                serialNumber = pos+1
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
        btn_saveSetup.setOnClickListener {
            //保存硬件编号
        }
    }
    interface hardwareSetupListener {
        fun saveSetupClick(text: String,serialPortNum: Int)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as hardwareSetupListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }

    }
    private fun saveSetupClick(text: String,serialPortNum: Int) {
        activityCallback?.saveSetupClick(text,serialPortNum)
    }

}// Required empty public constructor
