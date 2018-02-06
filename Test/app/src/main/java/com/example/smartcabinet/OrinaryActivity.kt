package com.example.smartcabinet

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_admin.*

class OrinaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        val oryfrag = OrinaryFragment()
        replaceFragment(oryfrag,R.id.framelayout)
        back_button.setOnClickListener(
                {
                    Toast.makeText(this,"点击了", Toast.LENGTH_SHORT).show()
                    val intent = Intent()
                    intent.setClass(this,LoginActivity::class.java)
                    startActivity(intent)

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
