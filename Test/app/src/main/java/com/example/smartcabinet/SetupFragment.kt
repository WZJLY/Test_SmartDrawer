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

    var activityCallback: SetupFragment.setuplisten ?= null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_setup, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_binding.setOnClickListener {
            setupClick("binding")
        }

        btn_setbox.setOnClickListener {
            setupClick("setbox")
        }

        btn_hardware.setOnClickListener {
            setupClick("hardware")
        }

        btn_update.setOnClickListener {

        }
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as setuplisten
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement setuplisten")
        }
    }

    interface setuplisten{
        fun setupClick(text: String)
    }

    private fun setupClick(text: String) {
        activityCallback?.setupClick(text)
    }
}// Required empty public constructor
