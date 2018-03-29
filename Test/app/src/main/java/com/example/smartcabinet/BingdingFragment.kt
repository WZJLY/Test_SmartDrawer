package com.example.smartcabinet


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.smartcabinet.util.DBManager
import kotlinx.android.synthetic.main.fragment_bingding.*


/**
 * A simple [Fragment] subclass.
 */
class BingdingFragment : Fragment() {

    private var dbManager: DBManager? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_bingding, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbManager = DBManager(context)
        if(dbManager!!.cabinetNo.size > 0) {
            bingding_et_number.setText(dbManager!!.cabinetNo[0].cabinetNo)
            bingding_et_number.isEnabled = false
            bingding_et_number.isFocusable = false
            bingding_et_number.isFocusableInTouchMode = false

            bingding_et_serviceCode.setText(dbManager!!.cabinetNo[0].cabinetServiceCode)
            bingding_et_serviceCode.isEnabled = false
            bingding_et_serviceCode.isFocusable = false
            bingding_et_serviceCode.isFocusableInTouchMode = false
            bingding_save.setText("解除绑定")
        }
        bingding_save.setOnClickListener {
            if(dbManager!!.cabinetNo.size > 0) {
                dbManager?.deleteAllCabinetNo()
                bingding_et_number.isEnabled = true
                bingding_et_number.isFocusable = true
                bingding_et_number.isFocusableInTouchMode = true
                bingding_et_serviceCode.isEnabled = true
                bingding_et_serviceCode.isFocusable = true
                bingding_et_serviceCode.isFocusableInTouchMode = true
                bingding_save.setText("绑定")
            }
            else {
                if (bingding_et_number.length()>0) {
                    if (bingding_et_serviceCode.length()>0) {
                        dbManager?.addCabinetNo(bingding_et_number.text.toString(),bingding_et_serviceCode.text.toString())
                        bingding_et_number.isEnabled = false
                        bingding_et_number.isFocusable = false
                        bingding_et_number.isFocusableInTouchMode = false
                        bingding_et_serviceCode.isEnabled = false
                        bingding_et_serviceCode.isFocusable = false
                        bingding_et_serviceCode.isFocusableInTouchMode = false
                        bingding_save.setText("解除绑定")
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
    }

}// Required empty public constructor
