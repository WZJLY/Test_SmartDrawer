package com.example.smartcabinet


import android.content.Intent

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import com.example.smartcabinet.util.*

import com.google.zxing.integration.android.IntentIntegrator



class OperationActivity : BaseActivity(),UserReagentFragment.userReagentListen,AdminReagentFragment.adminReagentListen {
    private var scApp: SCApp? = null
    private var dbManager: DBManager? = null
    private var drawer: Drawer? = null
    private var reagentTemplate: ReagentTemplate? = null
    private var statue: String? = null
    private var camera: android.hardware.Camera? = null
    var spi: SerialPortInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operation)
        dbManager = DBManager(applicationContext)
        val cabinetFragment = CabinetFragment()
        addFragment(R.id.Layout_cabinet, cabinetFragment)

        scApp = application as SCApp
        changeMessage("noFocusable")
        updateDrawer()
        if(dbManager!!.sysSeting.size > 0){
            val serialPortNum = dbManager!!.sysSeting[0].serialNum.toInt()
            val SPID = resources.getStringArray(R.array.serialPort)
            val serialPortID = SPID[serialPortNum]
            Log.d("串口操作",serialPortID)
//            spi = SerialPortInterface(this.applicationContext, serialPortID)
//            scApp?.setSpi(spi)
        }
        else
            Toast.makeText(this,"请进行系统硬件设置",Toast.LENGTH_SHORT).show()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode === KeyEvent.KEYCODE_BACK) {
            return true // return true 和 false 我都试过，都能屏蔽，原因还未知，希望知道的可以告诉我一下，谢谢
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun userReagentButtonClick(text: String) {
        when (text) {
            "userTake" -> {
                val intent = Intent()
                intent.setClass(this, SubOperationActivity::class.java)
                intent.putExtra("subOperation", "Take")

                startActivity(intent)
            }

            "userReturn" -> {
                val intent = Intent()
                intent.setClass(this, SubOperationActivity::class.java)
                intent.putExtra("subOperation", "Return")
                startActivity(intent)
            }

            "userBack" -> {
                val intent = Intent()
                intent.setClass(this, AdminActivity::class.java)
                startActivity(intent)
            }
        }
    }


    override fun adminReagentButtonClick(text: String) {
        when (text) {
            "Into" -> {
                statue = "Into"
                if (camera == null) {
                    try {
                        camera = android.hardware.Camera.open(0)
                    } catch (e: Exception) {
                        Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                    }
                }
                if(camera!=null) {
                    camera?.release()
                    try {
                        val integrator = IntentIntegrator(this)
                        spi?.sendLED(1,1)
                        integrator.setOrientationLocked(false)
                        integrator.setBeepEnabled(true)
                        integrator.captureActivity = SmallCaptureActivity::class.java
                        integrator.setTimeout(10000)
                        integrator.initiateScan()           //打开扫码活动，扫码时间为10s，扫码完成或者10s时间到，转到ActivityResult
                    } catch (e: Exception) {
                        Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                    }
                }
                else
                    Toast.makeText(this,"Cannot connect Camera!!", Toast.LENGTH_LONG).show()

            }

            "adminTake" -> {
                val intent = Intent()
                intent.setClass(this, SubOperationActivity::class.java)
                intent.putExtra("subOperation", "Take")
                startActivity(intent)
            }

            "adminReturn" -> {
                statue = "Return"
                if (camera == null) {
                    try {
                        camera = android.hardware.Camera.open(0)
                    } catch (e: Exception) {
                        Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                    }
                }
                if(camera!=null) {
                    camera?.release()
                    try {
                        val integrator = IntentIntegrator(this)
                        spi?.sendLED(1,1)
                        integrator.setOrientationLocked(false)
                        integrator.captureActivity = SmallCaptureActivity::class.java
                        integrator.setTimeout(10000)
                        integrator.initiateScan()           //打开扫码活动，扫码时间为10s，扫码完成或者10s时间到，转到ActivityResult
                    } catch (e: Exception) {
                        Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                    }
                }
                else
                    Toast.makeText(this,"Cannot connect Camera!!", Toast.LENGTH_LONG).show()

            }

            "Scrap" -> {
                val intent = Intent()
                intent.setClass(this, SubOperationActivity::class.java)
                intent.putExtra("subOperation", "Scrap")
                startActivity(intent)
            }

            "adminBack" -> {
                val intent = Intent()
                intent.setClass(this, AdminActivity::class.java)
                startActivity(intent)
            }
        }
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }


    fun updateDrawer()          //通过遍历数据库更新抽屉界面
    {
        val arrListDrawers = dbManager?.getDrawers()
        val sum = arrListDrawers!!.size.toInt()
        if (sum == 0) {
            Toast.makeText(this, "请添加抽屉", Toast.LENGTH_SHORT).show()
        } else {
            if (sum > 0) {
                for (i in 1..sum) {
                    drawer = arrListDrawers?.get(i - 1)
                    val drawerFragment2 = DrawerFragment2()
                    val args = Bundle()
                    args.putInt("drawerID", drawer!!.getId())
                    drawerFragment2.setArguments(args)
                    addFragment(R.id.Layout_drawer, drawerFragment2)

                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        try {

            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)    //获取扫码结果
            val intent = Intent()
            intent.setClass(this, SubOperationActivity::class.java)
            if (statue == "Into") {
                spi?.sendLED(1,0)
                intent.putExtra("subOperation", "Into")     //跳转到入柜fragment
            }
            if (statue == "Return"){
                spi?.sendLED(1,0)
                intent.putExtra("subOperation", "Return")//跳转到归还fragment
            }
            intent.putExtra("scan_value", result.contents)
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
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    fun changeMessage(text: String) {        //根据点击的位置，改变下方的功能栏
        val buttonStyle = Bundle()
        Log.d("Operation", text)
        buttonStyle.putString("buttonStyle", text)
        val adminReagentFragment = AdminReagentFragment()
        adminReagentFragment.arguments = buttonStyle
        replaceFragment(R.id.frameLayout_button, adminReagentFragment)
    }

    fun AppCompatActivity.replaceFragment(frameId: Int, fragment: Fragment) {
        supportFragmentManager.inTransaction { replace(frameId, fragment) }
    }

    fun AppCompatActivity.addFragment(frameId: Int, fragment: Fragment) {
        supportFragmentManager.inTransaction { add(frameId, fragment) }
    }
}
