package com.example.smartcabinet


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_setup.*


/**
 * A simple [Fragment] subclass.
 */
class SetupFragment : Fragment() {
    private var scApp: SCApp? = null
    var activityCallback: SetupFragment.setupFragmentListener?= null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        scApp = context.applicationContext as SCApp
        return inflater!!.inflate(R.layout.fragment_setup, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val serialPortID = scApp?.serialPortID
        eT_serialPort.setText(serialPortID)
        btn_serialPort.setOnClickListener {
            val id = eT_serialPort.text.toString()
            scApp?.setSerialPort(id)
            saveSetupClick("setupFragment")
        }
    }
    interface setupFragmentListener {
        fun saveSetupClick(text: String)
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as setupFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }

    }
    private fun saveSetupClick(text: String) {
        activityCallback?.saveSetupClick(text)
    }
}// Required empty public constructor
