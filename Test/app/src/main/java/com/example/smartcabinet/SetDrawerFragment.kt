package com.example.smartcabinet


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.Drawer
import kotlinx.android.synthetic.main.fragment_set_drawer.*


/**
 * A simple [Fragment] subclass.
 */
class SetDrawerFragment : Fragment() {
    var activityCallback: SetDrawerFragment.SetDrawerFragmentListener?= null
    private var dbManager: DBManager? = null
    private  var boxnum = 0
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
        val Madapter = ArrayAdapter.createFromResource(context, R.array.drawer, R.layout.spinner_style)
        Madapter?.setDropDownViewResource(R.layout.dropdown_style)
        spinner_drawer.adapter = Madapter
        spinner_drawer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View,
                                        pos: Int, id: Long) {
                val boxNum = resources.getStringArray(R.array.drawer)
                boxnum = boxNum[pos].toInt()
                val tableFragment = TableFragment()
                val arg = Bundle()
                arg.putInt("boxnum", boxnum)
                arg.putString("statue", "add")
                tableFragment.arguments = arg
                tableFragment.arguments = arg
                val fragmentTrasaction = fragmentManager.beginTransaction()
                fragmentTrasaction.replace(R.id.fL_table, tableFragment)
                fragmentTrasaction.commit()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        when(arguments.getString("data")){
            "find_out" ->{
                drawerID = arguments.getString("setDrawer").toInt()
                tv_drawerNum.text = "抽屉"+drawerID.toString()
                spinner_drawer.setSelection((dbManager!!.getDrawerByDrawerId(drawerID).drawerSize)-2)
                spinner_drawer.isEnabled = false
                setDrawer_btn_del.visibility = View.GONE
                btn_save.visibility = View.GONE
            }
            "drawer_modify" -> {
                drawerID = arguments.getString("setDrawer").toInt()
                tv_drawerNum.text = "抽屉"+drawerID.toString()
                spinner_drawer.setSelection((dbManager!!.getDrawerByDrawerId(drawerID).drawerSize)-2)
                if(dbManager!!.drawers.size!=drawerID){
                    setDrawer_btn_del.visibility = View.GONE
                }
                setDrawer_btn_del.setOnClickListener {
                    dbManager?.deleteDrawer(drawerID,1)
                    saveDrawerbuttonClicked("saveDrawer")
                }
                btn_save.setOnClickListener {
                    dbManager?.udpateDrawerSize(drawerID,1,boxnum)
                    saveDrawerbuttonClicked("saveDrawer")
                }
            }
            "setDrawer" -> {
                spinner_drawer.setSelection(0)
                val arrayList = dbManager?.drawers
                if (arrayList == null) {
                    drawerID = 1
                } else {
                    drawerID = arrayList!!.size+1
                }
                tv_drawerNum.text = "抽屉"+drawerID.toString()
                btn_save.setOnClickListener {
                    dbManager?.addDrawer(drawerID, 1, boxnum,"0")
                    saveDrawerbuttonClicked("saveDrawer")
                }
                setDrawer_btn_del.visibility = View.GONE
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
