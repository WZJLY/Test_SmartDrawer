package com.example.smartcabinet


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.ReagentUserRecord
import kotlinx.android.synthetic.main.fragment_record_line.*


/**
 * A simple [Fragment] subclass.
 */
class RecordLineFragment : Fragment() {
    private var dbmanager: DBManager? = null
    private var reagentId: String? = null
    private var reagentUserRecord: ReagentUserRecord? = null
    private var scApp: SCApp? = null
    private var date:String?=null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_record_line, container, false)
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        scApp = context.applicationContext as SCApp
        dbmanager= DBManager(context.applicationContext)
        if(arguments!=null)
        {
            date = arguments.getString("date")
            reagentUserRecord=dbmanager?.getReagentUseRecordByDate(date)
            record_ID.text=reagentUserRecord?.reagentId
            when(reagentUserRecord?.operationType){
                1 ->{
                    record_type.text = "入柜"
                }
                2->{
                    record_type.text = "取用"
                }
                3->{
                    record_type.text = "归还"
                }
                4->{
                    record_type.text = "移除"
                }
            }
            record_time.text=reagentUserRecord?.operationTime
            record_user.text=reagentUserRecord?.operator
            record_weigh.text=reagentUserRecord?.reagentTotalSize
            record_allowance.text=reagentUserRecord?.reagentSize
            record_wastage.text=reagentUserRecord?.consumption
        }
    }
}// Required empty public constructor
