package com.example.smartcabinet


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.Reagent
import kotlinx.android.synthetic.main.fragment_information3.*


/**
 * A simple [Fragment] subclass.
 */
class InformationFragment3 : Fragment() {
    private var dbManager: DBManager? = null
    private var reagent: Reagent?=null
private var scApp: SCApp? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_information3, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager = DBManager(context.applicationContext)
        scApp = context.applicationContext as SCApp
             reagent = dbManager?.getReagentByPos(""+scApp!!.getTouchdrawer(),""+scApp?.getTouchtable())
            tV_name.text=reagent?.reagentName
            tV_purity.text=reagent?.reagentPurity+"%"
            tV_data.text=reagent?.reagentInvalidDate
            tV_manufactor.text=reagent?.reagentCreater
        if(reagent?.reagentUnit==1)tV_residue.text=reagent?.reagentSize+"g"
        else tV_residue.text=reagent?.reagentSize+"ml"
            tV_num.text = reagent?.reagentId
            tV_person.text=reagent?.reagentUser     //取用时的信息显示

    }
}// Required empty public constructor

