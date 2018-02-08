package com.example.smartcabinet


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.smartcabinet.R.id.iBt_addpetson
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.SC_Const
import com.example.smartcabinet.util.UserAccount
import kotlinx.android.synthetic.main.fragment_add_preson.*
import kotlinx.android.synthetic.main.fragment_cabinet.*
import kotlinx.android.synthetic.main.fragment_line_person.*


/**
 * A simple [Fragment] subclass.
 */
class EditPersonFragment : Fragment() {
    var activityCallback: EditPersonFragment.addpersonbuttonlisten? = null
    private var dbManager: DBManager? = null
    private var userAccount: UserAccount? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_add_preson, container, false)
    }

    interface addpersonbuttonlisten {
        fun addpersonButtonClick(text: String)
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as addpersonbuttonlisten
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement buttonlisten")
        }

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dbManager = DBManager(context.applicationContext)
        updateUser()
        iBt_addpetson.setOnClickListener{
            addbuttonClicked("addperson")

        }
    }
    private fun addbuttonClicked(text: String) {
        activityCallback?.addpersonButtonClick(text)
    }



    fun updateUser()
    {
        val arrayList = dbManager?.getUsers()
        val sum = arrayList!!.size.toInt()
        for(i in 1..sum)
        {
            userAccount = arrayList?.get(i-1)
            val fragment = childFragmentManager.beginTransaction()
            val personFragment =PersonLineFragment()
            val args = Bundle()
            args.putString("username",userAccount?.getUserName().toString())
            personFragment.setArguments(args)
            fragment.add(R.id.Layout_person, personFragment,userAccount?.getUserName())
            fragment.commit()

        }
    }
    fun removeUser()
    {
        var removeUsername = String()
        if(getArguments()!=null)
        {
            removeUsername = getArguments().getString("rmUsername")

        }
        val removepfrg = childFragmentManager.findFragmentByTag(removeUsername)
        val fragment = childFragmentManager.beginTransaction()
        fragment.remove(removepfrg)
    }
}
