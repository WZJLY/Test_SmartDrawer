package com.example.smartcabinet

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_edit_person.*
import kotlinx.android.synthetic.main.serach_fragment.*

/**
 * Created by WZJ on 2018/2/6.
 */
class EditMessageFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_edit_person, container, false)

    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
            button_save.setOnClickListener{

            }
    }
}