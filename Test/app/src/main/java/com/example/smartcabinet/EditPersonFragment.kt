package com.example.smartcabinet


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartcabinet.R.id.iBt_addpetson
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.SC_Const
import com.example.smartcabinet.util.UserAccount
import kotlinx.android.synthetic.main.fragment_add_preson.*


/**
 * A simple [Fragment] subclass.
 */
class EditPersonFragment : Fragment() {
    private var dbManager: DBManager? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_add_preson, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        iBt_addpetson.setOnClickListener{
            dbManager = DBManager(context)
            dbManager?.tableUpgrade()
            val userAccount = UserAccount()

        userAccount.userId="3"
        userAccount.userPower= SC_Const.NORMAL
        userAccount.userName="wzj"
        userAccount.userPassword="123"
        dbManager?.addAccount(userAccount)
            val intent  = Intent()
            intent.setClass(context.applicationContext,EditMessageActivity::class.java)
            startActivity(intent)
        }



    }
}// Required empty public constructor
