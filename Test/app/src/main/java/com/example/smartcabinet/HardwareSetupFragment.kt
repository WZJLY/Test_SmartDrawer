package com.example.smartcabinet


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
        var serialNumber = 0
        var cameraNum = 0
        dbManager = DBManager(context)
        if(dbManager!!.sysSeting.size > 0) {
            serialNumber = dbManager!!.sysSeting[0].serialNum.toInt()
            cameraNum = dbManager!!.sysSeting[0].cameraVersion.toInt()
            Log.d("sysSeting",serialNumber.toString() + "," + cameraNum.toInt())
        }
        val SerialportAdapter = ArrayAdapter.createFromResource(context, R.array.serialPort, R.layout.spinner_style)
        SerialportAdapter?.setDropDownViewResource(R.layout.dropdown_style)
        sp_serialPort.adapter = SerialportAdapter
        sp_serialPort.setSelection(serialNumber)
        sp_serialPort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View,
                                        pos: Int, id: Long) {
                serialNumber = pos
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
        val CameraAdapter = ArrayAdapter.createFromResource(context, R.array.camera, R.layout.spinner_style)
        CameraAdapter?.setDropDownViewResource(R.layout.dropdown_style)
        sp_camera.adapter = CameraAdapter
        sp_camera.setSelection(cameraNum)
        sp_camera.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View,
                                        pos: Int, id: Long) {
                cameraNum = pos
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

        btn_saveSetup.setOnClickListener {
            if(dbManager!!.sysSeting.size > 0) {
                dbManager?.deleteAllSysSeting()
            }
            dbManager?.addSysSeting(serialNumber.toString(),cameraNum.toString())
            saveHardware("saveHardware")
        }
    }
    interface hardwareSetupListener {
        fun saveHardwareClick(text: String)
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
    private fun saveHardware(text: String) {
        activityCallback?.saveHardwareClick(text)
    }

}// Required empty public constructor
