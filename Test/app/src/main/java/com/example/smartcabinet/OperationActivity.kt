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
import com.example.lib_zxing.activity.CaptureActivity
import com.example.lib_zxing.activity.CodeUtils
import com.example.smartcabinet.util.*

class OperationActivity : BaseActivity(),UserReagentFragment.userReagentListen,AdminReagentFragment.adminReagentListen {
    private var scApp: SCApp? = null
    private var dbManager: DBManager? = null
    private var statue: String? = null
    var spi: SerialPortInterface? = null
    private var REQUEST_CODE = 1
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
            spi = SerialPortInterface(this.applicationContext, serialPortID)
            scApp?.setSpi(spi)
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
                spi?.sendLED(1, 1)
                var intent = Intent(this, CaptureActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE)
            }
            "adminTake" -> {
                val intent = Intent()
                intent.setClass(this, SubOperationActivity::class.java)
                intent.putExtra("subOperation", "Take")
                startActivity(intent)
            }

            "adminReturn" -> {
                statue = "Return"
                spi?.sendLED(1, 1)
                var intent = Intent(this, CaptureActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE)
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
        val sum = dbManager?.drawers!!.size
        if (sum == 0) {
            Toast.makeText(this, "请添加抽屉", Toast.LENGTH_SHORT).show()
        } else {
            if (sum > 0) {
                for (i in 1..sum) {
                    val drawerFragment2 = DrawerFragment2()
                    val args = Bundle()
                    args.putInt("drawerID", i)
                    drawerFragment2.arguments = args
                    addFragment(R.id.Layout_drawer, drawerFragment2)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        spi?.sendLED(1,0)
        val intent = Intent()
        intent.setClass(this, SubOperationActivity::class.java)
        intent.putExtra("subOperation", statue)
        if (requestCode == REQUEST_CODE) {
            if (null != data) {
                val bundle = data.extras
                if (bundle == null) {
                    Toast.makeText(this, "EMPTY", Toast.LENGTH_LONG).show()
                    return
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    val result = bundle.getString(CodeUtils.RESULT_STRING)
                    intent.putExtra("scan_value", result)
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show()
                }
            }
        }
        startActivity(intent)
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
