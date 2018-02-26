package com.example.smartcabinet

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.Drawer
import com.example.smartcabinet.util.SC_Const


class OperationActivity : AppCompatActivity(),UserReagentFragment.userReagentListen,AdminReagentFragment.adminReagentListen {
    private var scApp: SCApp? = null
    private var dbManager:DBManager?=null
    private var drawer: Drawer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operation)
        dbManager = DBManager(applicationContext)
        val cabinetFragment = CabinetFragment()
        addFragment(R.id.Layout_cabinet,cabinetFragment)

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
        updateDrawer()

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
    fun updateDrawer()
    {
        val arrListDrawers = dbManager?.getDrawers()
        if(arrListDrawers == null)
        {
            Toast.makeText(this, "请添加抽屉", Toast.LENGTH_SHORT).show()
        }


        else
        {
            val sum = arrListDrawers!!.size.toInt()
            if(sum>0) {
                for (i in 1..sum) {
                    drawer = arrListDrawers?.get(i - 1)
                    val fragment =fragmentManager.beginTransaction()
                    val drawerFragment2 = DrawerFragment2()
                    val args = Bundle()
                    args.putInt("drawerID", drawer!!.getId().toInt())
                    drawerFragment2.setArguments(args)
                    addFragment(R.id.Layout_drawer,drawerFragment2)

                }
            }
        }
    }
}
