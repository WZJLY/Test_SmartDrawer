package com.example.smartcabinet

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentContainer
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.smartcabinet.R.array.drawer
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.Reagent
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.admin_fragment.*
import kotlinx.android.synthetic.main.serach_fragment.*

/**
 * Created by WZJ on 2018/1/31.
 */

class SerachFragment : Fragment() {
    private var dbManager: DBManager? = null
    private var reagent:Reagent?=null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity.title = "试剂查找"
        return inflater!!.inflate(R.layout.serach_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) { //搜索功能，功能待开发与完善
        dbManager = DBManager(context.applicationContext)
         val arrayListReagent =  dbManager?.reagents
        if(arrayListReagent!=null)
        {
            val sum = arrayListReagent.size
            if(sum>0) {
                for (i in 1..sum) {
                    reagent = arrayListReagent?.get(i - 1)
                    val fragment = childFragmentManager.beginTransaction()
                    val serachReagentsFragment =  SearchReagentsFragment()
                    val args = Bundle()
                    args.putString("reagentID", reagent?.reagentId)
                    serachReagentsFragment.arguments=args
                    fragment.add(R.id.add_drawer, serachReagentsFragment)
                    fragment.commit()

                }
            }
        }

 }







}
