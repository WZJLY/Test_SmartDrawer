package com.example.smartcabinet

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentContainer
import android.support.v4.app.FragmentManager
import android.text.TextWatcher
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
import android.text.Editable



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
                    val serachReagentsFragment = SearchReagentsFragment()
                    val args = Bundle()
                    args.putString("reagentID", reagent?.reagentId)
                    serachReagentsFragment.arguments = args
                    fragment.add(R.id.add_drawer, serachReagentsFragment)
                    fragment.commit()


                }
            }
        }
        serach_view.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {



            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) { //屏蔽回车 中英文空格
            if(s.length==0)
            {
                if(arrayListReagent!=null)
                {
                val sum = arrayListReagent.size
                if(sum>0) {
                    for (i in 1..sum) {
                        if(i==1) {
                            reagent = arrayListReagent?.get(i - 1)
                            val fragment = childFragmentManager.beginTransaction()
                            val serachReagentsFragment = SearchReagentsFragment()
                            val args = Bundle()
                            args.putString("reagentID", reagent?.reagentId)
                            serachReagentsFragment.arguments = args
                            fragment.replace(R.id.add_drawer, serachReagentsFragment)
                            fragment.commit()
                        }
                        else
                        {
                            reagent = arrayListReagent?.get(i - 1)
                            val fragment = childFragmentManager.beginTransaction()
                            val serachReagentsFragment = SearchReagentsFragment()
                            val args = Bundle()
                            args.putString("reagentID", reagent?.reagentId)
                            serachReagentsFragment.arguments = args
                            fragment.add(R.id.add_drawer, serachReagentsFragment)
                            fragment.commit()
                }

                }
        }
    }
}
                else
               {
                   if(arrayListReagent!=null)
                   {
                       val sum = arrayListReagent.size
                       if(sum>0) {
                           var statue =0
                           for (i in 1..sum) {
                               reagent = arrayListReagent?.get(i - 1)
                               if(reagent!!.reagentName.contains(s.toString()))
                               {
                                   val fragment = childFragmentManager.beginTransaction()
                                   val serachReagentsFragment = SearchReagentsFragment()
                                   val args = Bundle()
                                   args.putString("reagentID", reagent?.reagentId)
                                   serachReagentsFragment.arguments = args
                                   if(statue==0) {
                                       statue=1
                                       fragment.replace(R.id.add_drawer, serachReagentsFragment)
                                   }
                                   else
                                       fragment.add(R.id.add_drawer, serachReagentsFragment)
                                   fragment.commit()
                               }
                           }
                       }
                   }

               }
            }
        })


 }







}
