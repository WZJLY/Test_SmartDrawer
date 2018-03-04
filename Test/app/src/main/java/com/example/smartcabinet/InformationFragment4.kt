package com.example.smartcabinet


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.Drawer
import com.example.smartcabinet.util.Reagent
import kotlinx.android.synthetic.main.fragment_information4.*


/**
 * A simple [Fragment] subclass.
 */
class InformationFragment4 : Fragment() {
    var dbManager: DBManager? = null
    private var reagent: Reagent?=null
    var drawerID:String?=null
    var pos:String?=null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_information4, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager = DBManager(context.applicationContext)
        if(arguments!=null)
        {
            drawerID  =  arguments.getString("tablenum")
           pos =  arguments.getString("pos")
          reagent =  dbManager?.getReagentByPos(drawerID,pos)
            tV_name2.text=reagent?.reagentName
            tV_purity2.text=reagent?.reagentPurity
            tV_data2.text=reagent?.reagentInvalidDate
            tV_manufactor2.text=reagent?.reagentCreater
            tV_residue2.text=reagent?.reagentSize
            tV_num2.text = reagent?.reagentId
            tV_person2.text=reagent?.reagentUser
        }


    }
}// Required empty public constructor
