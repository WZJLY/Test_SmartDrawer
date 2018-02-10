package com.example.smartcabinet

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.example.smartcabinet.util.SC_Const


class OperationActivity : AppCompatActivity() {
    private var scApp: SCApp? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operation)

        scApp = application as SCApp
        val userAccount =  scApp?.getUserInfo()
        when(userAccount?.getUserPower()){
            SC_Const.ADMIN ->
            {
                val adminReagentFragment = AdminReagentFragment()
                replaceFragment(R.id.frameLayout_button,adminReagentFragment)
            }
            SC_Const.NORMAL ->
            {
                val userReagentFragment = UserReagentFragment()
                replaceFragment(R.id.frameLayout_button, userReagentFragment)
            }
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
