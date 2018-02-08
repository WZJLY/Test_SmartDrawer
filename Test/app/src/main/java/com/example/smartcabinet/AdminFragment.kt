package com.example.smartcabinet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.admin_fragment.*
import kotlinx.android.synthetic.main.orinary_fragment.*

@Suppress("UNREACHABLE_CODE")
/**
 * Created by WZJ on 2018/1/31.
 */

class AdminFragment : Fragment() {
    var activityCallback: AdminFragment.buttonlisten? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.admin_fragment, container, false)

    }
    interface buttonlisten {
        fun onButtonClick(text: String)
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as buttonlisten
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement buttonlisten")
        }

    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        reagent_search.setOnClickListener {
            buttonClicked("reagent_search")
        }
        personal_management.setOnClickListener{

            buttonClicked("personal_management")
        }
        editflie.setOnClickListener{

            buttonClicked(" editflie")
        }
        reagent_op.setOnClickListener{
            buttonClicked("reagent_op")
        }
        recordquery.setOnClickListener{
            buttonClicked("recordquery")
        }
        setBox_button.setOnClickListener{
            buttonClicked("setBox_button")
        }
        auto_update.setOnClickListener{
            buttonClicked("auto_update")
        }
        reagent_template.setOnClickListener{
            buttonClicked("reagent_templatep")
        }

    }
    private fun buttonClicked(text: String) {
        activityCallback?.onButtonClick(text)
    }

}
