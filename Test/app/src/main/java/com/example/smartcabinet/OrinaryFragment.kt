package com.example.smartcabinet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.orinary_fragment.*

/**
 * Created by WZJ on 2018/1/31.
 */

class OrinaryFragment : Fragment() {

    var activityCallback: OrinaryFragment.orinarybuttonlisten? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.orinary_fragment, container, false)
    }
    interface orinarybuttonlisten {
        fun orinaryButtonClick(text: String)
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as orinarybuttonlisten
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }

    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {

        regentsearch?.setOnClickListener {
            orinarybuttonClicked("regentsearch")
        }
        edit_flie?.setOnClickListener{
            orinarybuttonClicked("edit_flie")
        }
        reagent_operation?.setOnClickListener{
            orinarybuttonClicked("reagent_operation")
        }
        record_query?.setOnClickListener{

        }
    }
    private fun orinarybuttonClicked(text: String) {
        activityCallback?.orinaryButtonClick(text)
    }

}
