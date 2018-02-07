package com.example.smartcabinet

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.fragment_line_person.*

/**
 * Created by WZJ on 2018/2/7.
 */
class PersonLineFragment : Fragment() {
    var text = String()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if(getArguments()!=null)
        {
            text = getArguments().getString("username")

        }
        return inflater!!.inflate(R.layout.fragment_line_person, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        iBt_deletPerson.setOnClickListener({

        })
        tV_userName.text=text
    }



}
