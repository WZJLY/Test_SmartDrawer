package com.example.smartcabinet


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_bingding.*


/**
 * A simple [Fragment] subclass.
 */
class BingdingFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_bingding, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bingding_save.setOnClickListener {
            if (bingding_et_number.length()>0) {
                if (bingding_et_serviceCode.length()>0) {
                   //保存数据
                }
                else
                    Toast.makeText(context, "服务码未填写", Toast.LENGTH_SHORT).show()
            }
            else {
                if (bingding_et_serviceCode.length()>0)
                    Toast.makeText(context, "智能柜编号未填写", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(context, "智能柜编号和服务码未填写", Toast.LENGTH_SHORT).show()
            }
        }
    }

}// Required empty public constructor
