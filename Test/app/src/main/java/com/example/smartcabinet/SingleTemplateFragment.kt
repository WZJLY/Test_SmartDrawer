package com.example.smartcabinet


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_single_template.*


/**
 * A simple [Fragment] subclass.
 */
class SingleTemplateFragment : Fragment() {
    var activityCallback: SingleTemplateFragment.singleTemplateListen? = null

    interface singleTemplateListen {
        fun singleTemplateButtonClick(text: String)
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_single_template, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        template_btn_save.setOnClickListener{
            singleTemplateClicked("btn_save")
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

    private fun singleTemplateClicked(text: String) {
        activityCallback?.singleTemplateButtonClick(text)
    }
}// Required empty public constructor
