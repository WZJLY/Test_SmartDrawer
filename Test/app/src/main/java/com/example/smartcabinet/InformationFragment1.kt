package com.example.smartcabinet

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_information1.*


/**
 * A simple [Fragment] subclass.
 */
class InformationFragment1 : Fragment() {

    var activityCallback:InformationFragment1.return_scanbuttonlisten? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_information1, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        if(arguments.getString("scan_value")!==null)
        {
            val value = arguments.getString("scan_value")
            eT_code.setText(value)
            eT_weight.isFocusable = true
            eT_weight.isFocusableInTouchMode = true
            eT_weight.requestFocus()
        }
        if(arguments.getString("weight")!==null){
            eT_weight.setText(arguments.getString("weight"))
        }
        btn_code.setOnClickListener{
            return_scanbuttononClicked("scan")
        }

    }
    interface return_scanbuttonlisten {
        fun return_scanbuttononClick(text: String)
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as return_scanbuttonlisten
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }

    }
    private fun  return_scanbuttononClicked(text: String) {
        activityCallback?.return_scanbuttononClick(text)
    }

}
