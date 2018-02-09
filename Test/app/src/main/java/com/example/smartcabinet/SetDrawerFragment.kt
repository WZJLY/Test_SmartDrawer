package com.example.smartcabinet


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.Drawer
import kotlinx.android.synthetic.main.fragment_set_drawer.*


/**
 * A simple [Fragment] subclass.
 */
class SetDrawerFragment : Fragment() {
    var activityCallback: SetDrawerFragment.SetDrawerFragmentListener?= null
    var isSpinnerFirst = true
    private var dbManager: DBManager? = null
    private var drawer: Drawer? = null
    var boxnum = 0
    var sum = 0
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
        dbManager = DBManager(context.applicationContext)
        spinner_drawer.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View,
                                        pos: Int, id: Long) {
                if (isSpinnerFirst) {
                    isSpinnerFirst = false
                }
                else{
                    val boxNum = resources.getStringArray(R.array.drawer)
                    tableFragment.addNum(boxNum[pos].toInt())
                    boxnum=boxNum[pos].toInt()
                    Toast.makeText(context, "你点击的是:" + boxNum[pos], Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        btn_save.setOnClickListener{
            val arrayList = dbManager?.getDrawers()
            if(arrayList == null)
            {
                sum = 0
                dbManager?.addDrawer(sum+1,1,boxnum)
            }
            else
                sum =arrayList!!.size.toInt()
                dbManager?.addDrawer(sum+1,1,boxnum)
            saveDrawerbuttonClicked("saveDrawer")


        }
    }
    interface SetDrawerFragmentListener {
        fun saveDrawerButtonClick(text: String)
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as SetDrawerFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }

    }
    private fun saveDrawerbuttonClicked(text: String) {
        activityCallback?.saveDrawerButtonClick(text)
    }

}// Required empty public constructor
