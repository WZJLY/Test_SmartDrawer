package com.example.smartcabinet

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.Drawer
import kotlinx.android.synthetic.main.fragment_set_cabinet.*


class SetCabinetFragment : Fragment() {
    var activityCallback: SetCabinetFragment.SetCabinetListener ?= null
    private var dbManager: DBManager? = null

    interface SetCabinetListener {
        fun setCabinetClick(fragment: String)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as SetCabinetListener
        }catch (e: ClassCastException) {
            Log.d("data","ERROR")
        }
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        activity.title = "抽屉设置"
        val cabinetFragment = CabinetFragment()
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.Layout_cabinet,cabinetFragment)
        fragmentTransaction.commit()
        return inflater!!.inflate(R.layout.fragment_set_cabinet, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager = DBManager(context.applicationContext)
        updateDrawer()
        val arrListDrawers = dbManager?.getDrawers()
        if(arrListDrawers == null)
        {
            Toast.makeText(context.applicationContext, "请添加抽屉", Toast.LENGTH_SHORT).show()
        }


        iBt_addCabinet.setOnClickListener {
//            val cabinetFragment = CabinetFragment()
//            val fragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.add(R.id.Layout_cabinet,cabinetFragment)
//            fragmentTransaction.commit()

        }

        iBt_addDrawer.setOnClickListener {
            activityCallback?.setCabinetClick("setDrawer")
        }
    }
    fun updateDrawer()
    {
        val arrListDrawers = dbManager?.getDrawers()
        val sum = arrListDrawers!!.size
        if(sum > 0)
        {
            for (i in 1..sum){
                val fragment = childFragmentManager.beginTransaction()
                val drawerFragment1 = DrawerFragment1()
                val args = Bundle()
                args.putInt("drawerID", i)
                drawerFragment1.arguments = args
                fragment.add(R.id.Layout_drawer, drawerFragment1, "抽屉")
                fragment.commit()
            }
        }
        else
        {
            Toast.makeText(context.applicationContext, "请添加抽屉", Toast.LENGTH_SHORT).show()
        }
    }
}// Required empty public constructor
