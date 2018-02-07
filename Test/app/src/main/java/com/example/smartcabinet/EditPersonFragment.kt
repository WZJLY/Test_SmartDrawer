package com.example.smartcabinet


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
import kotlinx.android.synthetic.main.fragment_line_person.*


/**
 * A simple [Fragment] subclass.
 */
class EditPersonFragment : Fragment() {
    private var dbManager: DBManager? = null
    private var userAccount: UserAccount? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_add_preson, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager = DBManager(context.applicationContext)
       val arrayList = dbManager?.getUsers()
        userAccount = arrayList?.get(0)

        val sum = arrayList!!.size.toInt()
        Toast.makeText(context.applicationContext,userAccount?.getUserName(),Toast.LENGTH_LONG).show()
for(i in 1..sum)
{
    val fragment = childFragmentManager.beginTransaction()
    val tfrg = PersonLineFragment()
    fragment.add(R.id.Layout_person, tfrg, "TAG")
    fragment.commit()
}
        iBt_addpetson.setOnClickListener{
            val intent  = Intent()
            intent.setClass(context.applicationContext,EditMessageActivity::class.java)
            startActivity(intent)

        }




    }
}// Required empty public constructor
