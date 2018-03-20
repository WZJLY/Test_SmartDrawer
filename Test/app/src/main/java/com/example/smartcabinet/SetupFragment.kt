package com.example.smartcabinet


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
        val Madapter = ArrayAdapter.createFromResource(context, R.array.serialPort, R.layout.spinner_style)
        Madapter?.setDropDownViewResource(R.layout.dropdown_style)
        sp_serialPort.adapter = Madapter
        sp_serialPort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View,
                                        pos: Int, id: Long) {

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
        btn_setup.setOnClickListener {

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
