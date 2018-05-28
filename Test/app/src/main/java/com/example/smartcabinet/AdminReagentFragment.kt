package com.example.smartcabinet


import android.content.Context

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartcabinet.R.id.*
import kotlinx.android.synthetic.main.fragment_admin_reagent.*


/**
 * A simple [Fragment] subclass.
 */
class AdminReagentFragment : Fragment() {

    var actionCallback: AdminReagentFragment.adminReagentListen ?= null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            actionCallback = context as adminReagentListen
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString() + "must implement adminReagentListen")
        }

    }

    interface adminReagentListen{
        fun adminReagentButtonClick(text: String)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_admin_reagent, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonStyle = arguments?.getString("buttonStyle")
        if(buttonStyle != null) {
            changeAdminButton(buttonStyle)
        }

        btn_into.setOnClickListener {
            buttonClicked("Into")
        }

        btn_admin_take.setOnClickListener {
            buttonClicked("adminTake")
        }

        btn_admin_return.setOnClickListener {
            buttonClicked("adminReturn")
        }

        btn_scrap.setOnClickListener {
            buttonClicked("Scrap")
        }

        btn_admin_back.setOnClickListener {
            buttonClicked("adminBack")
        }
    }

    private fun buttonClicked(text: String) {
        actionCallback?.adminReagentButtonClick(text)
    }

    fun changeAdminButton(text: String){
        when(text){
            "noFocusable" -> {
                btn_into?.isEnabled = false
                btn_admin_take.isEnabled = false
                btn_admin_return.isEnabled = true
                btn_scrap.isEnabled = false
            }

            "style_denglu" -> {
                btn_into.isEnabled = true
                btn_admin_take.isEnabled = false
                btn_admin_return.isEnabled = true
                btn_scrap.isEnabled = false
            }
            "style1" -> {
                btn_into.isEnabled = false
                btn_admin_take.isEnabled = true
                btn_admin_return.isEnabled = false
                btn_scrap.isEnabled = true
            }

            "style2" -> {
                btn_into.isEnabled = false
                btn_admin_take.isEnabled = false
                btn_admin_return.isEnabled = true
                btn_scrap.isEnabled = true
            }
        }
    }
}// Required empty public constructor
