package com.example.smartcabinet


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.ReagentUserRecord

/**
 * A simple [Fragment] subclass.
 */
class RecordFragment : Fragment() {

    private var dbManager: DBManager? = null
    private var reagentUserRecord: ReagentUserRecord? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        activity.title = "记录查询"
        return inflater!!.inflate(R.layout.fragment_record, container, false)
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) { //搜索功能，功能待开发与完善
        dbManager = DBManager(context.applicationContext)
        val arrayListReagentUserRecord = dbManager?.reagentUseRecord
        if (arrayListReagentUserRecord != null) {
            val sum = arrayListReagentUserRecord.size
            if (sum > 0) {
                for (i in sum..1) {
                    reagentUserRecord = arrayListReagentUserRecord?.get(i - 1)
                    val fragment = childFragmentManager.beginTransaction()
                    val recordLineFragment = RecordLineFragment()
                    val args = Bundle()
                    args.putString("date", reagentUserRecord?.operationTime)
                    recordLineFragment.arguments = args
                    fragment.add(R.id.add_record, recordLineFragment)
                    fragment.commit()

                }
            }
        }

    }

}// Required empty public constructor
