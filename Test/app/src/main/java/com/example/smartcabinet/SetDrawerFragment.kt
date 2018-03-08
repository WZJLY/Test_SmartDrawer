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
    private  var boxnum = 0
    private  var sum = 0
    private  var drawerID = 0
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_set_drawer, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager = DBManager(context.applicationContext)
        val tableFragment = TableFragment()
        val fragmentTrasaction = fragmentManager.beginTransaction()
        fragmentTrasaction.replace(R.id.fL_table,tableFragment)
        fragmentTrasaction.commit()
       if(getArguments().getString("setDrawer")!=null&&getArguments().getString("setDrawer")!="set")
       {

           drawerID = getArguments().getString("setDrawer").toInt()
           val tableFragment = TableFragment()
           val arg = Bundle()
           arg.putInt("tablenum",drawerID)
           arg.putString("statue","drawer")
           tableFragment.arguments = arg
           val fragmentTrasaction = fragmentManager.beginTransaction()
           fragmentTrasaction.replace(R.id.fL_table,tableFragment)
           fragmentTrasaction.commit()

           spinner_drawer.setSelection( (dbManager!!.getDrawerByDrawerId(drawerID).getDrawerSize())-2)
           spinner_drawer.isEnabled = false
           btn_save.setVisibility(View.GONE)
       }

        if(getArguments().getString("setDrawer")=="set") {
            spinner_drawer.setSelection(0)
            spinner_drawer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View,
                                            pos: Int, id: Long) {
                    val boxNum = resources.getStringArray(R.array.drawer)

                    boxnum = boxNum[pos].toInt()

                    val tableFragment = TableFragment()
                    val arg = Bundle()
                    arg.putInt("boxnum",boxnum)
                    arg.putString("statue","add")
                    tableFragment.setArguments(arg)
                    tableFragment.arguments = arg
                    val fragmentTrasaction = fragmentManager.beginTransaction()
                    fragmentTrasaction.replace(R.id.fL_table,tableFragment)
                    fragmentTrasaction.commit()

                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            }
            btn_save.setOnClickListener {
                val arrayList = dbManager?.drawers
                if (arrayList == null) {
                    sum = 0
                    dbManager?.addDrawer(sum + 1, 1, boxnum)
                } else {
                    sum = arrayList!!.size
                    dbManager?.addDrawer(sum + 1, 1, boxnum)
                }
                saveDrawerbuttonClicked("saveDrawer")

            }
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
