package com.example.smartcabinet

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.admin_fragment.*

@Suppress("UNREACHABLE_CODE")
/**
 * Created by WZJ on 2018/1/31.
 */

class AdminFragment : Fragment() {
    var activityCallback: AdminFragmentListener?= null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity.title = "安储智能试剂柜"
        return inflater!!.inflate(R.layout.admin_fragment, container, false)

    }

    interface AdminFragmentListener {
        fun adminOnButtonClick(text: String)
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as AdminFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        reagent_search.setOnClickListener {
            adminButtonClicked("reagent_search")
        }
        personal_management.setOnClickListener{

            adminButtonClicked("personal_management")
        }
        editflie.setOnClickListener{
            adminButtonClicked(" editflie")
        }
        reagent_op.setOnClickListener{
          adminButtonClicked("reagent_op")
        }
        recordquery.setOnClickListener{
            adminButtonClicked("recordquery")
        }

        reagent_template.setOnClickListener{
            adminButtonClicked("reagent_template")
        }
        btn_setup.setOnClickListener {
            adminButtonClicked("btn_setup")
        }

    }
    private fun adminButtonClicked(text: String) {
        activityCallback?.adminOnButtonClick(text)
    }
}
