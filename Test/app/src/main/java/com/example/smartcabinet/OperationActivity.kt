package com.example.smartcabinet

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.smartcabinet.util.*
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.admin_fragment.*
import kotlin.math.log


class OperationActivity : AppCompatActivity(),UserReagentFragment.userReagentListen,AdminReagentFragment.adminReagentListen{
    private var scApp: SCApp? = null
    private var dbManager:DBManager?=null
    private var drawer: Drawer? = null
    private var reagentTemplate:ReagentTemplate? = null
    private var statue:String?= null
    var spi: SerialPortInterface ?= null

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
        spi = SerialPortInterface(this.applicationContext)
        scApp?.setSpi(spi)
    }


    override fun userReagentButtonClick(text: String) {
        when(text) {
            "userTake" -> {
                    val intent = Intent()
                    intent.setClass(this, SubOperationActivity::class.java)
                    intent.putExtra("subOperation", "Take")

                    startActivity(intent)




            }

            "userReturn" -> {
                val intent = Intent()
                intent.setClass(this,SubOperationActivity::class.java)
                intent.putExtra("subOperation","Return")
                startActivity(intent)
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
                    statue = "Into"
                    try {
                        val integrator = IntentIntegrator(this)
                        integrator.setOrientationLocked(false)
                        integrator.captureActivity = SmallCaptureActivity::class.java
                        integrator.setTimeout(10000)
                        integrator.initiateScan()
                    } catch (e: Exception) {
                        Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                    }

            }

            "adminTake" -> {
                val intent = Intent()
                intent.setClass(this,SubOperationActivity::class.java)
                intent.putExtra("subOperation","Take")
                startActivity(intent)
            }

            "adminReturn" -> {
                statue = "Return"
                try {
                    val integrator = IntentIntegrator(this)
                    integrator.setOrientationLocked(false)
                    integrator.captureActivity = SmallCaptureActivity::class.java
                    integrator.setTimeout(10000)
                    integrator.initiateScan()
                }
                catch (e: Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                }

            }

            "Scrap" -> {
                val intent = Intent()
                intent.setClass(this,SubOperationActivity::class.java)
                intent.putExtra("subOperation","Scrap")
                startActivity(intent)
            }

            "adminBack" -> {
                val intent = Intent()
                intent.setClass(this,AdminActivity::class.java)
                startActivity(intent)
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

    fun AppCompatActivity.addFragment(frameId: Int, fragment: Fragment) {
        supportFragmentManager.inTransaction{add(frameId, fragment)}
    }

    fun updateDrawer()
    {
        val arrListDrawers = dbManager?.getDrawers()
        val sum = arrListDrawers!!.size.toInt()
        if(sum == 0)
        {
            Toast.makeText(this, "请添加抽屉", Toast.LENGTH_SHORT).show()
        }


        else
        {
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        try {

                val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
                val intent = Intent()
                intent.setClass(this, SubOperationActivity::class.java)
                if(statue=="Into") {
                    intent.putExtra("subOperation", "Into")
                }
                if(statue=="Return") intent.putExtra("subOperation","Return")
                intent.putExtra("scan_value",result.contents)
                startActivity(intent)

            if (result != null) {
                if (result.contents == null) {
                    //Log.d("MainActivity", "Cancelled scan")
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()

                } else {
                    //Log.d("MainActivity", "Scanned")
                    Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                }
            } else {
                // This is important, otherwise the result will not be passed to the fragment
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
        catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }
}
