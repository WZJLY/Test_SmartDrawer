package com.example.smartcabinet


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_drawer2.*


/**
 * A simple [Fragment] subclass.
 */
class DrawerFragment2 : Fragment() {

    private var drawerID  = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if(getArguments()!=null)
        {
            drawerID = getArguments().getInt("drawerID")
        }
        return inflater!!.inflate(R.layout.fragment_drawer2, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        tV_drawer2.text =("抽屉"+drawerID)
        iBt_drawer2.setOnClickListener {
        if(childFragmentManager.findFragmentByTag("table")==null) {

        val tableFragment = TableFragment()
        val args = Bundle()
        args.putString("statue","drawer")
        args.putInt("tablenum", drawerID)
        tableFragment.setArguments(args)
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.linearLayout_drawer, tableFragment, "table")
        fragmentTransaction.commit()

    }
    else
    {

        val tableFragment = childFragmentManager.findFragmentByTag("table")
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.remove(tableFragment)
        fragmentTransaction.commit()

    }


        }
        iBt_lock.setOnClickListener{

            //发送开锁指令
        }

    }

}// Required empty public constructor
