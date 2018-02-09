package com.example.smartcabinet


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_set_drawer.*


/**
 * A simple [Fragment] subclass.
 */
class SetDrawerFragment : Fragment() {
    var isSpinnerFirst = true
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_set_drawer, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val tableFragment = TableFragment()
        val fragmentTrasaction = fragmentManager.beginTransaction()
        fragmentTrasaction.replace(R.id.fL_table,tableFragment)
        fragmentTrasaction.commit()

        spinner_drawer.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View,
                                        pos: Int, id: Long) {
                if (isSpinnerFirst) {
                    isSpinnerFirst = false
                }
                else{
                    val boxNum = resources.getStringArray(R.array.drawer)
                    tableFragment.addNum(boxNum[pos].toInt())
                    Toast.makeText(context, "你点击的是:" + boxNum[pos], Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
    }

}// Required empty public constructor
