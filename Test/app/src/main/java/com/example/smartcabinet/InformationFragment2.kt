package com.example.smartcabinet


import android.content.Context
import android.os.Bundle
import android.renderscript.Sampler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_information2.*


/**
 * A simple [Fragment] subclass.
 */
class InformationFragment2 : Fragment() {


    var activityCallback:InformationFragment2.scanbuttonlisten? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_information2, container, false)

    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
       if(getArguments().getString("scan_value")!==null)
       {
           val value = getArguments().getString("scan_value")
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
