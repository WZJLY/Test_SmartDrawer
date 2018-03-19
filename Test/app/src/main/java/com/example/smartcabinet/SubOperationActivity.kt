package com.example.smartcabinet

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.ReagentTemplate
import com.example.smartcabinet.util.SerialPortInterface
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_sub_operation.*
import kotlinx.android.synthetic.main.fragment_information1.*
import kotlinx.android.synthetic.main.fragment_information2.*
import java.text.SimpleDateFormat
import java.util.*

class SubOperationActivity : AppCompatActivity(),InformationFragment2.scanbuttonlisten,InformationFragment1.return_scanbuttonlisten{
private var statue:String?=null
    private var scApp: SCApp? = null
    private var dbManager:DBManager?=null
    private var reagentTemplate:ReagentTemplate?=null
    var spi: SerialPortInterface?= null
    private var camera: android.hardware.Camera? = null

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(null != this.currentFocus) {
            val mInputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0)
        }
        return super.onTouchEvent(event)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_operation)
        scApp = application as SCApp
        dbManager = DBManager(applicationContext)
        val subOperation: String = intent.getStringExtra("subOperation")
        val scan_value = intent.getStringExtra("scan_value")
        spi =  scApp?.getSpi()

        when(subOperation) {
            "Into" -> {
                num.text="抽屉"+scApp!!.touchdrawer.toString()
                val tableFragment = TableFragment()
                val arg = Bundle()
                arg.putString("statue","op")
                arg.putInt("tablenum_op", scApp!!.touchdrawer)
                arg.putString("touch","false")
                tableFragment.arguments=arg
                replaceFragment(R.id.FL_table,tableFragment)

                val informationFragment = InformationFragment2()
                val args = Bundle()
                args.putString("scan_value", scan_value)
                informationFragment.setArguments(args)
                replaceFragment(R.id.frameLayout_information,informationFragment)

            }

            "Take" -> {
                num.text="抽屉"+scApp!!.touchdrawer.toString()
                val tableFragment = TableFragment()
                val arg = Bundle()
                arg.putString("statue","op")
                arg.putInt("tablenum_op", scApp!!.getTouchdrawer())
                arg.putString("touch","false")
                tableFragment.arguments=arg
                replaceFragment(R.id.FL_table,tableFragment)

                val informationFragment = InformationFragment3()
                replaceFragment(R.id.frameLayout_information,informationFragment)

            }

            "Return" -> {
                val informationFragment = InformationFragment1()
                val args = Bundle()
                args.putString("scan_value", scan_value)
                informationFragment.setArguments(args)
                replaceFragment(R.id.frameLayout_information,informationFragment)
            }

            "Scrap" -> {
                num.text="抽屉"+scApp!!.touchdrawer.toString()
                val tableFragment = TableFragment()
                val arg = Bundle()
                arg.putString("statue","op")
                arg.putInt("tablenum_op", scApp!!.getTouchdrawer())
                arg.putString("touch","false")
                tableFragment.arguments=arg
                replaceFragment(R.id.FL_table,tableFragment)

                val informationFragment = InformationFragment3()
                replaceFragment(R.id.frameLayout_information,informationFragment)
            }
        }

        btn_back.setOnClickListener {
            val intent = Intent()
            intent.setClass(this,OperationActivity::class.java)
            startActivity(intent)
        }
        btn_ok.setOnClickListener{
            val drawerID = scApp!!.touchdrawer
            when(subOperation)
            {
                "Into" -> {

                    var into_drawer = checkLock(1,1)
                    if (into_drawer == null)
                        Toast.makeText(this,"ERROR：串口通讯！",Toast.LENGTH_SHORT).show()
                    else if(into_drawer != drawerID && into_drawer > 0)
                            Toast.makeText(this, " 请关闭" + (into_drawer) + "号抽屉", Toast.LENGTH_SHORT).show()
                    else
                    {
                        if(into_drawer != drawerID) {
                            spi?.sendOpenLock(1, drawerID)
                            Toast.makeText(this, " 请拉开" + (drawerID) + "号抽屉", Toast.LENGTH_SHORT).show()
                            into_drawer = checkLock(1,190)
                        }
                        if (into_drawer == drawerID) {
                            val dialog = AlertDialog.Builder(this)
                                    .setTitle("入柜")
                                    .setMessage("请放入试剂后点击确定")
                                    .setPositiveButton("确定", DialogInterface.OnClickListener { dialogInterface, i ->
                                        if(!dbManager!!.isReagentExist(eT_code.text.toString())&&!dbManager!!.isScrapReagentExist(eT_code.text.toString())) {
                                            reagentTemplate = dbManager!!.reagentTemplate.get(scApp!!.templateNum)
                                            var Unit = 1
                                            if (reagentTemplate?.reagentUnit == "ml") {
                                                Unit = 2
                                            }
                                            dbManager?.addReagent(eT_code.text.toString(), reagentTemplate?.reagentName, "", ""
                                                    , "", 1, reagentTemplate?.reagentPurity, eT_residue.text.toString(), eT_weight2.text.toString()
                                                    , reagentTemplate?.reagentCreater, reagentTemplate?.reagentGoodsID, Unit, reagentTemplate?.reagentDensity, eT_data.text.toString()
                                                    , "1", drawerID.toString(), scApp?.touchtable.toString(), 1, scApp!!.userInfo.getUserName())
                                            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                                            val now = sdf.format(Date())
                                            dbManager?.addReagentUserRecord(eT_code.text.toString(),1,now,scApp!!.userInfo.getUserName(),eT_weight2.text.toString()+"g",eT_residue.text.toString()+reagentTemplate?.reagentUnit,"")
                                            val intent = Intent()
                                            scApp?.touchtable = 0
                                            scApp?.touchtable = 0 //新加的
                                            intent.setClass(this, OperationActivity::class.java)
                                            startActivity(intent)
                                        }
                                        else
                                            Toast.makeText(this,"该试剂编码已使用",Toast.LENGTH_SHORT).show()

                                    })
                                    .setNeutralButton("取消", null)
                                    .create()
                            dialog.show()
                            dialog.window.setGravity(Gravity.CENTER)
                        }
                    }
                }

                "Take" -> {
                    var into_drawer = checkLock(1, 1)
                    if (into_drawer == null)
                        Toast.makeText(this,"ERROR：串口通讯！",Toast.LENGTH_SHORT).show()
                    else if(into_drawer != drawerID && into_drawer > 0)
                        Toast.makeText(this, " 请关闭" + (into_drawer) + "号抽屉", Toast.LENGTH_SHORT).show()
                    else {
                        if (into_drawer != drawerID) {
                            spi?.sendOpenLock(1, drawerID)
                            Toast.makeText(this, " 请拉开" + (drawerID) + "号抽屉", Toast.LENGTH_SHORT).show()
                            into_drawer = checkLock(1,190)
                        }
                        if (into_drawer == drawerID) {
                            val dialog = AlertDialog.Builder(this)
                                    .setTitle("取用")
                                    .setMessage("请取出试剂后点击确定")
                                    .setPositiveButton("确定", DialogInterface.OnClickListener { dialogInterface, i ->
                                        dbManager?.updateReagentStatusByPos("" + drawerID, "" + scApp?.touchtable, scApp!!.userInfo.getUserName(), 2)
                                        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                                        val now = sdf.format(Date())
                                        val reagentId =  dbManager!!.getReagentByPos("" + drawerID,"" + scApp?.touchtable).reagentId
                                        val reagent = dbManager?.getReagentById(reagentId)
                                        var unit = "g"
                                        if(reagent?.reagentUnit==2)
                                            unit = "ml"
                                        dbManager?.addReagentUserRecord(reagentId,2,now,scApp!!.userInfo.getUserName(),reagent?.reagentTotalSize+"g",reagent?.reagentSize+unit,"")
                                        val intent = Intent()
                                        intent.setClass(this, OperationActivity::class.java)
                                        startActivity(intent)
                                    })
                                    .setNeutralButton("取消", null)
                                    .create()
                            dialog.show()
                            dialog.window.setGravity(Gravity.CENTER)
                        }
                    }
                }

                "Return" -> {
                    if(dbManager!!.isReagentExist(eT_code2.text.toString())&&!dbManager!!.isScrapReagentExist(eT_code2.text.toString()))
                    {
                        if(dbManager!!.getReagentById(eT_code2.text.toString()).status==2) {
                            if (eT_weight.text != null) {
                                var into_drawer = checkLock(1,1)
                                if (into_drawer == null)
                                    Toast.makeText(this,"ERROR：串口通讯！",Toast.LENGTH_SHORT).show()
                                else if(into_drawer != drawerID && into_drawer > 0)
                                    Toast.makeText(this, " 请关闭" + (into_drawer) + "号抽屉", Toast.LENGTH_SHORT).show()
                                else
                                {
                                    if(into_drawer != drawerID) {
                                        spi?.sendOpenLock(1, drawerID)
                                        Toast.makeText(this, " 请拉开" + (drawerID) + "号抽屉", Toast.LENGTH_SHORT).show()
                                        into_drawer = checkLock(1,190)
                                    }
                                    if(into_drawer == drawerID) {
                                        val reagent = dbManager?.getReagentById(eT_code2.text.toString())
                                        val dialog = OperationDialog(this)
                                        val num =dbManager!!.getDrawerByDrawerId(drawerID).drawerSize
                                        dialog.setTitle("归还")
                                        dialog.setMessage("请归还试剂后点击确定")
                                        dialog.setNum(num,reagent!!.reagentPosition.toInt())
                                        dialog.setYesOnclickListener("确定",object :OperationDialog.onYesOnclickListener{
                                            override fun onYesClick() {
                                                dbManager?.updateReagentStatus(eT_code2.text.toString(), 1, scApp!!.userInfo.getUserName())
                                                var weight:Int = Integer.valueOf(eT_weight.text.toString())
                                                if(weight>dbManager!!.getReagentById(eT_code2.text.toString()).reagentTotalSize.toInt())
                                                {
                                                    weight -=dbManager!!.getReagentById(eT_code2.text.toString()).reagentTotalSize.toInt()
                                                    var size =  dbManager!!.getReagentById(eT_code2.text.toString()).reagentSize.toDouble()-(weight*dbManager!!.getReagentById(eT_code2.text.toString()).reagentDensity.toDouble())
                                                    dbManager?.updateReagentSize(eT_code2.text.toString(),size.toString(),eT_weight.text.toString())
                                                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                                                    val now = sdf.format(Date())
                                                    var unit = "g"
                                                    if(reagent?.reagentUnit==2)
                                                        unit="ml"
                                                    dbManager?.addReagentUserRecord(eT_code2.text.toString(),3,now,scApp!!.userInfo.getUserName(),eT_weight.text.toString()+"g",size.toString()+unit,(weight*dbManager!!.getReagentById(eT_code2.text.toString()).reagentDensity.toDouble()).toString())
                                                }
                                                else
                                                {
                                                    weight =dbManager!!.getReagentById(eT_code2.text.toString()).reagentTotalSize.toInt()-weight

                                                    var size1 =  dbManager!!.getReagentById(eT_code2.text.toString()).reagentSize.toDouble()-(weight*dbManager!!.getReagentById(eT_code2.text.toString()).reagentDensity.toDouble())
                                                    dbManager?.updateReagentSize(eT_code2.text.toString(),size1.toString(),eT_weight.text.toString())
                                                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                                                    val now = sdf.format(Date())
                                                    var unit = "g"
                                                    if(reagent?.reagentUnit==2)
                                                        unit="ml"
                                                    dbManager?.addReagentUserRecord(eT_code2.text.toString(),3,now,scApp!!.userInfo.getUserName(),eT_weight.text.toString()+"g ",size1.toString()+unit,(weight*dbManager!!.getReagentById(eT_code2.text.toString()).reagentDensity.toDouble()).toString())
                                                }
                                                val intent = Intent()
                                                intent.setClass(applicationContext, OperationActivity::class.java)
                                                startActivity(intent)
                                                dialog.dismiss()
                                            }

                                                })
                                        dialog.setNoOnclickListener("取消",object :OperationDialog.onNoOnclickListener{
                                            override fun onNoClick() {
                                                dialog.dismiss()
                                            }
                                        })



                                        dialog.show()
                                    }
                                }
                            }
                            else
                                Toast.makeText(this,"称重重量未填写",Toast.LENGTH_SHORT).show()
                        }
                        else
                            Toast.makeText(this,"该试剂在位",Toast.LENGTH_SHORT).show()
                    }
                    else
                        Toast.makeText(this,"无该试剂",Toast.LENGTH_SHORT).show()

                }

                "Scrap" -> {
                    var into_drawer = checkLock(1,1)
                    if (into_drawer == null)
                        Toast.makeText(this,"ERROR：串口通讯！",Toast.LENGTH_SHORT).show()
                    else if(into_drawer != drawerID && into_drawer > 0)
                        Toast.makeText(this, " 请关闭" + (into_drawer) + "号抽屉", Toast.LENGTH_SHORT).show()
                    else {
                        if (into_drawer != drawerID) {
                            spi?.sendOpenLock(1, drawerID)
                            Toast.makeText(this, " 请拉开" + (drawerID) + "号抽屉", Toast.LENGTH_SHORT).show()
                            into_drawer = checkLock(1,190)
                        }
                        if (into_drawer == drawerID) {
                            val dialog = AlertDialog.Builder(this)
                                    .setTitle("报废")
                                    .setMessage("请取出试剂后点击确定")
                                    .setPositiveButton("确定", DialogInterface.OnClickListener{ dialogInterface, i ->
                                        val reagentId=dbManager!!.getReagentByPos("" + drawerID,"" + scApp?.touchtable).reagentId
                                        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                                        val now = sdf.format(Date())
                                        val reagent = dbManager?.getReagentById(reagentId)
                                        dbManager?.addSrapReagent(reagent?.reagentId,reagent?.reagentName,"","","",reagent!!.reagentType.toInt(),reagent?.reagentPurity,reagent?.reagentSize,reagent?.reagentTotalSize,reagent?.reagentCreater,"",reagent?.reagentUnit,reagent?.reagentDensity,reagent?.reagentInvalidDate,reagent?.reagentCabinetId,drawerID.toString(),reagent?.reagentPosition,4,scApp!!.userInfo.getUserName())
                                        dbManager?.deleteReagentById(reagentId)
                                        dbManager?.addReagentUserRecord(reagentId,4,now,scApp!!.userInfo.getUserName(),eT_weight?.text.toString(),"","")
                                        val intent = Intent()
                                        intent.setClass(this, OperationActivity::class.java)
                                        startActivity(intent)
                                    })
                                    .setNeutralButton("取消", null)
                                    .create()
                            dialog.show()
                            dialog.window.setGravity(Gravity.CENTER)
                        }
                    }
                }
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

    override fun scanbuttononClick(text: String) {
        if(text == "scan")
        {   statue="Into"
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
    }

    override fun return_scanbuttononClick(text: String) {
        if(text == "scan")
        {
            statue="Return"
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
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        try {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if(statue=="Into")
                eT_code.setText(result.contents)
                if(statue == "Return")
                    eT_code2.setText(result.contents)
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

    fun checkLock(DID: Int,num: Int): Int? {
        for(i in 1..num) {
            val lockData = spi?.sendGetStat(DID)
            if (lockData != null) {
                val drawerId = lockData.indexOf("1")+1
                Log.d("SubOperation",""+drawerId)
                if (drawerId > 0)
                    return drawerId
                else
                    continue
            }
            else
                return null
        }
        return 0
    }
}
