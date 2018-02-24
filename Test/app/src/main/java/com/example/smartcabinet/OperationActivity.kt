package com.example.smartcabinet

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.example.smartcabinet.util.SC_Const


class OperationActivity : AppCompatActivity(),UserReagentFragment.userReagentListen,AdminReagentFragment.adminReagentListen {
    private var scApp: SCApp? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operation)

        val cabinetFragment = CabinetFragment()
        addFragment(R.id.Layout_cabinet,cabinetFragment)

        val drawerFragment2 = DrawerFragment2()
        addFragment(R.id.Layout_drawer,drawerFragment2)

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

    override fun userReagentButtonClick(text: String) {
        when(text) {
            "userTake" -> {

            }

            "userReturn" -> {

            }

            "userBack" -> {
                val intent = Intent()
                intent.setClass(this,AdminActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun adminReagentButtonClick(text: String) {
        when(text) {
            "Into" -> {

            }

            "adminTake" -> {

            }

            "adminReturn" -> {

            }

            "Scrap" -> {

            }

            "adminBack" -> {
                val intent = Intent()
                intent.setClass(this,AdminActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun AppCompatActivity.replaceFragment(frameId: Int, fragment: Fragment) {
        supportFragmentManager.inTransaction{replace(frameId, fragment)}
    }

    fun AppCompatActivity.addFragment(frameId: Int, fragment: Fragment) {
        supportFragmentManager.inTransaction{add(frameId, fragment)}
    }
}
