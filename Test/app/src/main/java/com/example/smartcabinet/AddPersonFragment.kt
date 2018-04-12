package com.example.smartcabinet


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.UserAccount
import kotlinx.android.synthetic.main.fragment_add_preson.*


/**
 * A simple [Fragment] subclass.
 */
class AddPersonFragment : Fragment() {
    var activityCallback: AddPersonFragment.addpersonbuttonlisten? = null
    private var dbManager: DBManager? = null
    private var userAccount: UserAccount? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
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
                    + " must implement AdminFragmentListener")
        }

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dbManager = DBManager(context.applicationContext)
        updateUser()
        btn_addpetson.setOnClickListener{

            addbuttonClicked("addperson")

        }
    }
    private fun addbuttonClicked(text: String) {
        activityCallback?.addpersonButtonClick(text)
    }

    fun updateUser()
    {
        val arrayList = dbManager?.getUsers()
        val sum = arrayList!!.size
        for(i in 1..sum)
        {

            userAccount = arrayList?.get(i-1)
            val fragment = childFragmentManager.beginTransaction()
            val personFragment =PersonLineFragment()
            val args = Bundle()
            args.putString("userName",userAccount!!.getUserName().toString())
            personFragment.arguments=args
            fragment.add(R.id.LL_person, personFragment,userAccount?.getUserName())
            fragment.commit()

        }//通过遍历用户的数据表对片断进行添加
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
