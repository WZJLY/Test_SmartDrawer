package com.example.smartcabinet

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.Reagent
import com.example.smartcabinet.util.ReagentUserRecord

/**
 * Created by WZJ on 2018/3/14.
 */

class SerachRecordFragment : Fragment() {
    private var dbManager: DBManager? = null
    private var reagentUserRecord: ReagentUserRecord? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.serach_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) { //搜索功能，功能待开发与完善
        dbManager = DBManager(context.applicationContext)
        val arrayListReagentUserRecord = dbManager?.reagentUseRecord
        if (arrayListReagentUserRecord != null) {
            val sum = arrayListReagentUserRecord.size
            if (sum > 0) {
                for (i in 1..sum) {
                    reagentUserRecord = arrayListReagentUserRecord?.get(i - 1)
                    val fragment = childFragmentManager.beginTransaction()
                    val reagentUserRecordFragment = ReagentUserRecordFragment()
                    val args = Bundle()
                    args.putString("date", reagentUserRecord?.operationTime)
                    reagentUserRecordFragment.arguments = args
                    fragment.add(R.id.add_drawer, reagentUserRecordFragment)
                    fragment.commit()

                }
            }
        }

    }
}
