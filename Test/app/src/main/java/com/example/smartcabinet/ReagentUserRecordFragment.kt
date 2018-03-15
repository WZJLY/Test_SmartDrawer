package com.example.smartcabinet

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.Reagent
import com.example.smartcabinet.util.ReagentUserRecord
import kotlinx.android.synthetic.main.fragment_record.*
import kotlinx.android.synthetic.main.fragment_search_reagents.*

/**
 * Created by WZJ on 2018/3/14.
 */

class ReagentUserRecordFragment: Fragment() {
    private var dbmanager: DBManager? = null
    private var reagentId: String? = null
    private var reagentUserRecord: ReagentUserRecord? = null
    private var scApp: SCApp? = null
    private var date:String?=null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_record, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        scApp = context.applicationContext as SCApp
        dbmanager= DBManager(context.applicationContext)
        if(arguments!=null)
        {
            date = arguments.getString("date")
            reagentUserRecord=dbmanager?.getReagentUseRecordByDate(date)
            tV_record.text="试剂编号:"+reagentUserRecord?.reagentId+",操作者:"+reagentUserRecord?.operator+",操作时间:"+reagentUserRecord?.operationTime+",操作类型:"+reagentUserRecord?.operationType+",称重值:"+reagentUserRecord?.reagentTotalSize+",余量:"+reagentUserRecord?.reagentSize+",消耗量:"+reagentUserRecord?.consumption
        }
    }
}
