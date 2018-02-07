package com.example.smartcabinet

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.example.smartcabinet.util.DBManager
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.fragment_line_person.*


/**
 * Created by WZJ on 2018/2/5.
 */
class EditPersonActivity : AppCompatActivity() {
    private var dbManager: DBManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        val editperson = EditPersonFragment()
        supportFragmentManager.inTransaction {
           replace(R.id.framelayout,editperson)

        }

   back_button.setOnClickListener({
       val intent = Intent()
       intent.setClass(this,AdminActivity::class.java)
       startActivity(intent)
   })

    }
    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }


}