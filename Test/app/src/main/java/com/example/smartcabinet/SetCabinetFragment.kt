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
import com.example.smartcabinet.util.UserAccount
import kotlinx.android.synthetic.main.fragment_set_cabinet.*


class SetCabinetFragment : Fragment() {
    var activityCallback: SetCabinetFragment.SetCabinetListener ?= null
    private var dbManager: DBManager? = null
    private var drawer: Drawer? = null

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
        val sum = arrListDrawers!!.size.toInt()
        if(sum == 0)
        {
            Toast.makeText(context.applicationContext, "请添加抽屉", Toast.LENGTH_SHORT).show()
        }
     else
     {
         if(sum>0) {
             for (i in 1..sum) {
                 drawer = arrListDrawers?.get(i - 1)
                 val fragment = childFragmentManager.beginTransaction()
                 val drawerFragment1 = DrawerFragment1()
                 val args = Bundle()
                 args.putInt("drawerID", drawer!!.getId().toInt())
                 drawerFragment1.setArguments(args)
                 fragment.add(R.id.Layout_drawer, drawerFragment1, "抽屉")
                 fragment.commit()
             }
         }
        }
    }
}// Required empty public constructor
