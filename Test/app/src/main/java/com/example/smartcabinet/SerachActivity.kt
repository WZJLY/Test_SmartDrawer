package com.example.smartcabinet

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_admin.*

class SerachActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        val serachfrag = SerachFragment()
        val oryFrag = OrinaryFragment()
        val adminfrag = AdminFragment()
        val textfrag = TextFragment()
        supportFragmentManager.inTransaction {

            replace(R.id.framelayout,serachfrag)
        }



    }
    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }
}
