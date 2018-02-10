package com.example.smartcabinet


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartcabinet.util.DBManager
import kotlinx.android.synthetic.main.fragment_drawer1.*


/**
 * A simple [Fragment] subclass.
 */
class DrawerFragment1 : Fragment() {
    var activityCallback: DrawerFragment1.deletDrawerFragmentListener?= null
    var drawerID  = 0
    private var dbManager: DBManager? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
                if(getArguments()!=null)
                {
                    drawerID = getArguments().getInt("drawerID")
                }
        return inflater!!.inflate(R.layout.fragment_drawer1, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager = DBManager(context.applicationContext)
        iBt_drawer1.setOnClickListener {
            deletDrawerbuttonClicked("find_out")
        }
        iBt_delete.setOnClickListener{
            dbManager?.deleteDrawer(drawerID,1)
            deletDrawerbuttonClicked("delet")
        }
        tV_drawer1.text =("抽屉"+drawerID)
    }
    interface deletDrawerFragmentListener {
        fun deletDrawerButtonClick(text: String)
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as deletDrawerFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }

    }
    private fun deletDrawerbuttonClicked(text: String) {
        activityCallback?.deletDrawerButtonClick(text)
    }

}// Required empty public constructor
