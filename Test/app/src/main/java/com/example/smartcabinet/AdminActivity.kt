package com.example.smartcabinet

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.admin_fragment.*

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        val adminfrag = AdminFragment()
        replaceFragment(adminfrag, R.id.framelayout)
        back_button.setOnClickListener(
                {Toast.makeText(this,"点击了", Toast.LENGTH_SHORT).show()
                val oryfrag = OrinaryFragment()
                replaceFragment(oryfrag,R.id.framelayout)

        })

    }
    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }


    fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction{replace(frameId, fragment)}
    }

    }




