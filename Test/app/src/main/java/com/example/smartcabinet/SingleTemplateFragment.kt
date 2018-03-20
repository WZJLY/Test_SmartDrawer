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
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_single_template.*


/**
 * A simple [Fragment] subclass.
 */
class SingleTemplateFragment : Fragment() {
    var state:Int = 0
    var activityCallback: SingleTemplateFragment.singleTemplateListen? = null

    interface singleTemplateListen {
        fun singleTemplateButtonClick(text: String, state: Int)
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_single_template, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val template_state = ArrayAdapter.createFromResource(context, R.array.template_type, R.layout.information_spinner_style)
        template_state?.setDropDownViewResource(R.layout.information_dropdown_style)
        template_sp_state.adapter=template_state
        template_sp_state.setSelection(0)
        template_sp_state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View,
                                        pos: Int, id: Long) {
                state = pos + 1
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        template_btn_save.setOnClickListener{
            if (template_et_name.length()>0) {
                if (template_et_volume.length()>0)
                    singleTemplateClicked("btn_save", state)
                else
                    Toast.makeText(context,"试剂瓶容量未填写",Toast.LENGTH_SHORT).show()
            }
            else {
                if (template_et_volume.length()>0)
                    Toast.makeText(context, "试剂名未填写", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(context, "试剂名和试剂瓶容量未填写", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as singleTemplateListen
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }
    }

    private fun singleTemplateClicked(text: String,state: Int) {
        activityCallback?.singleTemplateButtonClick(text,state)
    }
}// Required empty public constructor
