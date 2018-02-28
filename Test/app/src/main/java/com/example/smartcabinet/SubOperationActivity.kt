package com.example.smartcabinet

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_sub_operation.*

class SubOperationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_operation)

        val subOperation: String = intent.getStringExtra("subOperation")

        when(subOperation) {
            "Into" -> {
                val informationFragment = InformationFragment2()
                replaceFragment(R.id.frameLayout_information,informationFragment)
            }

            "Take" -> {
                val informationFragment = InformationFragment3()
                replaceFragment(R.id.frameLayout_information,informationFragment)
            }

            "Return" -> {
                val informationFragment = InformationFragment1()
                replaceFragment(R.id.frameLayout_information,informationFragment)
            }

            "Scrap" -> {
                val informationFragment = InformationFragment3()
                replaceFragment(R.id.frameLayout_information,informationFragment)
            }
        }

        btn_back.setOnClickListener {
            val intent = Intent()
            intent.setClass(this,OperationActivity::class.java)
            startActivity(intent)
        }
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }

    fun AppCompatActivity.replaceFragment(frameId: Int, fragment: Fragment) {
        supportFragmentManager.inTransaction{replace(frameId, fragment)}
    }


}
