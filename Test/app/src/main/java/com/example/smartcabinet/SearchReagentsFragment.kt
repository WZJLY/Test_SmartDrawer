package com.example.smartcabinet


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.Reagent
import kotlinx.android.synthetic.main.fragment_search_reagents.*


/**
 * A simple [Fragment] subclass.
 */
class SearchReagentsFragment : Fragment() {
    private var dbmanager: DBManager?=null
    private var reagentId:String?=null
    private var reagent:Reagent?=null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_search_reagents, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbmanager= DBManager(context.applicationContext)
        if(arguments!=null)
        {   var unit:String?=null
            reagentId=arguments.getString("reagentID")
            reagent=dbmanager?.getReagentById(reagentId)
            if(reagent?.reagentUnit==1) unit = "g"
            else unit="ml"
            tV_reagents.text=reagent?.reagentName+","+reagent?.reagentSize+unit+","+"纯度:"+reagent?.reagentPurity+"%，"+reagent?.reagentCreater+","+reagent?.reagentInvalidDate
        }
    }

}
