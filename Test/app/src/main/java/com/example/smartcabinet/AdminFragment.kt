package com.example.smartcabinet

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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.admin_fragment, container, false)

    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        reagent_search.setOnClickListener {
            val intent  = Intent()
            intent.setClass(context.applicationContext,SerachActivity::class.java)
            startActivity(intent)
        }
        personal_management.setOnClickListener{
            val intent = Intent()
            intent.setClass(context.applicationContext,EditPersonActivity::class.java)
            startActivity(intent)
        }
        editflie.setOnClickListener{
            val intent = Intent()
            intent.setClass(context.applicationContext,EditMessageActivity::class.java)
            startActivity(intent)
        }
        reagent_op.setOnClickListener{

        }
        recordquery.setOnClickListener{

        }
        setBox_button.setOnClickListener{

        }
        auto_update.setOnClickListener{

        }
        reagent_template.setOnClickListener{

        }

    }

}
