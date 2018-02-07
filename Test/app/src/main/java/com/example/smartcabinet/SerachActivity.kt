package com.example.smartcabinet

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.webkit.JavascriptInterface
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.SC_Const
import com.example.smartcabinet.util.UserAccount
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.activity_login.*

class SerachActivity : AppCompatActivity() {
    private var scApp: SCApp? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        val serachfrag = SerachFragment()

        scApp = application as SCApp
        val account = scApp?.getUserInfo()
        supportFragmentManager.inTransaction{
            replace(R.id.framelayout,serachfrag)
        }
        back_button.setOnClickListener{
            val intent = Intent()


            if(account?.getUserPower()==0)
            {
                intent.setClass(this,AdminActivity::class.java)
                startActivity(intent)
            }
            else
            {

                intent.setClass(this,OrinaryActivity::class.java)
                startActivity(intent)
            }

        }



    }
    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }
}
