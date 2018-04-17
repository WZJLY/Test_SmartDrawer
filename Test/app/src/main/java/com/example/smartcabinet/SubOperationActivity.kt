package com.example.smartcabinet

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.lib_zxing.activity.CaptureActivity
import com.example.lib_zxing.activity.CodeUtils
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.ReagentTemplate
import com.example.smartcabinet.util.SerialPortInterface
import kotlinx.android.synthetic.main.activity_sub_operation.*
import kotlinx.android.synthetic.main.fragment_information1.*
import kotlinx.android.synthetic.main.fragment_information2.*
import java.text.SimpleDateFormat
import java.util.*

class SubOperationActivity : BaseActivity(),InformationFragment2.scanbuttonlisten,InformationFragment1.return_scanbuttonlisten{
    private var statue:String?=null
    private var scApp: SCApp? = null
    private var dbManager:DBManager?=null
    private var reagentTemplate:ReagentTemplate?=null
    var spi: SerialPortInterface?= null
    private var REQUEST_CODE = 1

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(null != this.currentFocus) {
            val mInputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return mInputMethodManager.hideSoftInputFromWindow(this.currentFocus.windowToken, 0)
        }
        return super.onTouchEvent(event)
    }

    //屏蔽虚拟后退按钮
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode === KeyEvent.KEYCODE_BACK) {
            return true // return true 和 false 我都试过，都能屏蔽，原因还未知，希望知道的可以告诉我一下，谢谢
        }
        return super.onKeyDown(keyCode, event)
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
                title = "入柜"
                informationFragment.arguments = args
                replaceFragment(R.id.frameLayout_information,informationFragment)

            }

            "Take" -> {
                num.text="抽屉"+scApp!!.touchdrawer.toString()
                val tableFragment = TableFragment()
                val arg = Bundle()
                arg.putString("statue","op")
                arg.putInt("tablenum_op", scApp!!.touchdrawer)
                arg.putString("touch","false")
                tableFragment.arguments=arg
                replaceFragment(R.id.FL_table,tableFragment)
                title = "取用"
                val informationFragment = InformationFragment3()
                replaceFragment(R.id.frameLayout_information,informationFragment)

            }

            "Return" -> {
                title = "归还"
                val informationFragment = InformationFragment1()
                val args = Bundle()
                args.putString("scan_value", scan_value)
                informationFragment.arguments = args
                replaceFragment(R.id.frameLayout_information,informationFragment)
            }

            "Scrap" -> {
                num.text="抽屉"+scApp!!.touchdrawer.toString()
                val tableFragment = TableFragment()
                val arg = Bundle()
                arg.putString("statue","op")
                arg.putInt("tablenum_op", scApp!!.touchdrawer)
                arg.putString("touch","false")
                tableFragment.arguments=arg
                replaceFragment(R.id.FL_table,tableFragment)
                title = "移除"
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
            var table = scApp!!.touchtable
            when(subOperation)
            {
                "Into" -> {
                    if(!dbManager!!.isReagentExist(eT_code2.text.toString())&&!dbManager!!.isScrapReagentExist(eT_code2.text.toString())){
                        var into_drawer = checkLock(1,1)
                        if (into_drawer == null)
                            Toast.makeText(this,"ERROR：串口通讯！",Toast.LENGTH_SHORT).show()
                        else if(into_drawer != drawerID && into_drawer > 0)
                            Toast.makeText(this, " 请关闭" + (into_drawer) + "号抽屉", Toast.LENGTH_SHORT).show()
                        else {
                            if(into_drawer != drawerID) {
                                spi?.sendOpenLock(1, drawerID)
                                Toast.makeText(this, " 请拉开" + (drawerID) + "号抽屉", Toast.LENGTH_SHORT).show()
                                into_drawer = checkLock(1,190)
                            }
                            if (into_drawer == drawerID) {
                                SubOperationDialog(subOperation,drawerID,table)
                            }
                        }
                    }
                    else
                        Toast.makeText(this,"该试剂编码已使用",Toast.LENGTH_SHORT).show()
                }

                "Take" -> {
                    var into_drawer = checkLock(1, 3)
                    if (into_drawer == null)
                        Toast.makeText(this,"ERROR：串口通讯！",Toast.LENGTH_SHORT).show()
                    else if(into_drawer != drawerID && into_drawer > 0)
                        Toast.makeText(this, " 请关闭" + (into_drawer) + "号抽屉", Toast.LENGTH_SHORT).show()
                    else {
                        if (into_drawer != drawerID) {
                            spi?.sendOpenLock(1, drawerID)
                            Toast.makeText(this, " 请拉开" + (drawerID) + "号抽屉", Toast.LENGTH_SHORT).show()
//                            Thread.sleep(50)
                            into_drawer = checkLock(1,190)
                        }
                        if (into_drawer == drawerID) {
                            SubOperationDialog(subOperation,drawerID,table)
                        }
                    }
                }

                "Return" -> {
                    if(dbManager!!.isReagentExist(eT_code.text.toString())&&!dbManager!!.isScrapReagentExist(eT_code.text.toString()))
                    {
                        if(dbManager!!.getReagentById(eT_code.text.toString()).status==2) {
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
//                                        Thread.sleep(50)
                                        into_drawer = checkLock(1,190)
                                    }
                                    if(into_drawer == drawerID) {
                                        table = dbManager?.getReagentById(eT_code.text.toString())!!.reagentPosition.toInt()
                                        SubOperationDialog(subOperation,drawerID,table)
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
//                            Thread.sleep(50)
                            into_drawer = checkLock(1,190)
                        }
                        if (into_drawer == drawerID) {
                            SubOperationDialog(subOperation,drawerID,table)
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
        if(text == "scan") {
            statue = "Into"
            spi?.sendLED(1, 1)
            var intent = Intent(this, CaptureActivity::class.java)
            startActivityForResult(intent,REQUEST_CODE)
        }
    }

    override fun return_scanbuttononClick(text: String) {
        if(text == "scan") {
            statue = "Return"
            spi?.sendLED(1, 1)
            var intent = Intent(this, CaptureActivity::class.java)
            startActivityForResult(intent,REQUEST_CODE)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        spi?.sendLED(1,0)
        if (requestCode == REQUEST_CODE) {
            if (null != data) {
                val bundle = data.extras
                if (bundle == null) {
                    Toast.makeText(this, "EMPTY", Toast.LENGTH_LONG).show()
                    return
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    val result = bundle.getString(CodeUtils.RESULT_STRING)
                    when(statue) {
                        "Into" -> {
                            eT_code2.setText(result)
                        }
                        "Return" -> {
                            eT_code.setText(result)
                        }
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    fun checkLock(DID: Int,num: Int): Int? {
        var time:Int = 0
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
            else {
                if (i > 1)
                    return null
                else{
                    time++
                    continue
                }
            }
        }
        return 0
    }

    fun SubOperationDialog(subOperation:String,drawerID:Int,table:Int) {
        val dialog = OperationDialog(this)
        val num =dbManager!!.getDrawerByDrawerId(drawerID).drawerSize
        dialog.setNum(num,table)
        when(subOperation){
            "Into" -> {
                dialog.setTitle("入柜")
                dialog.setMessage("请放入试剂后点击确定")
                dialog.setYesOnclickListener("确定",object :OperationDialog.onYesOnclickListener{
                    override fun onYesClick() {
                        reagentTemplate = dbManager!!.reagentTemplate.get(scApp!!.templateNum)
                        var Unit = 1
                        if (reagentTemplate?.reagentUnit == "ml") {
                            Unit = 2
                        }
                        dbManager?.addReagent(eT_code2.text.toString(), reagentTemplate?.reagentName, "", ""
                                , "", 1, reagentTemplate?.reagentPurity, eT_residue.text.toString(), eT_weight2.text.toString()
                                , reagentTemplate?.reagentCreater, reagentTemplate?.reagentGoodsID, Unit, reagentTemplate?.reagentDensity, eT_data.text.toString()
                                , "1", drawerID.toString(), scApp?.touchtable.toString(), 1, scApp!!.userInfo.getUserName())
                        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                        val now = sdf.format(Date())
                        dbManager?.addReagentUserRecord(eT_code2.text.toString(),1,now,scApp!!.userInfo.getUserName(),eT_weight2.text.toString()+"g",eT_residue.text.toString()+reagentTemplate?.reagentUnit,"")
                        val intent = Intent()
                        scApp?.touchtable = 0
                        scApp?.touchtable = 0 //新加的
                        intent.setClass(applicationContext, OperationActivity::class.java)
                        startActivity(intent)
                        dialog.dismiss()
                    }
                })
            }
            "Take" -> {
                dialog.setTitle("取用")
                dialog.setMessage("请取出试剂后点击确定")
                dialog.setYesOnclickListener("确定",object :OperationDialog.onYesOnclickListener{
                    override fun onYesClick() {
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
                        intent.setClass(applicationContext, OperationActivity::class.java)
                        startActivity(intent)
                        dialog.dismiss()
                    }
                })
            }
            "Return"->{
                val reagent = dbManager!!.getReagentById(eT_code.text.toString())
                dialog.setTitle("归还")
                dialog.setMessage("请归还试剂后点击确定")
                dialog.setYesOnclickListener("确定",object :OperationDialog.onYesOnclickListener{
                    override fun onYesClick() {
                        dbManager?.updateReagentStatus(eT_code.text.toString(), 1, scApp!!.userInfo.getUserName())
                        var weight:Int = Integer.valueOf(eT_weight.text.toString())
                        if(weight>reagent.reagentTotalSize.toInt())
                        {
                            weight -=reagent.reagentTotalSize.toInt()
                            Log.d("subOperation",reagent.reagentSize)
                            Log.d("subOperation1",reagent.reagentDensity.toString())
                            Log.d("subOperation",reagent.reagentSize.toDouble().toString())
                            var size =  reagent.reagentSize.toDouble() -(weight/ reagent.reagentDensity.toDouble())
                            dbManager?.updateReagentSize(eT_code.text.toString(),size.toString(),eT_weight.text.toString())
                            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                            val now = sdf.format(Date())
                            var unit = "g"
                            if(reagent?.reagentUnit==2)
                                unit="ml"
                            dbManager?.addReagentUserRecord(eT_code.text.toString(),3,now,scApp!!.userInfo.getUserName(),eT_weight.text.toString()+"g",size.toString()+unit,(weight*dbManager!!.getReagentById(eT_code.text.toString()).reagentDensity.toDouble()).toString())
                        }
                        else
                        {
                            weight =reagent.reagentTotalSize.toInt()-weight
                            Log.d("subOperation",reagent.reagentSize)
                            Log.d("subOperation1",reagent.reagentDensity.toString())
                            Log.d("subOperation",reagent.reagentSize.toDouble().toString())
                            var size1 =  reagent.reagentSize.toDouble() -(weight/ reagent.reagentDensity.toDouble())
                            dbManager?.updateReagentSize(eT_code.text.toString(),size1.toString(),eT_weight.text.toString())
                            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                            val now = sdf.format(Date())
                            var unit = "g"
                            if(reagent?.reagentUnit==2)
                                unit="ml"
                            dbManager?.addReagentUserRecord(eT_code.text.toString(),3,now,scApp!!.userInfo.getUserName(),eT_weight.text.toString()+"g ",size1.toString()+unit,(weight*dbManager!!.getReagentById(eT_code.text.toString()).reagentDensity.toDouble()).toString())
                        }
                        val intent = Intent()
                        intent.setClass(applicationContext, OperationActivity::class.java)
                        startActivity(intent)
                        dialog.dismiss()
                    }

                })
            }
            "Scrap"-> {
                dialog.setTitle("移除")
                dialog.setMessage("请取出试剂后点击确定")
                dialog.setYesOnclickListener("确定",object :OperationDialog.onYesOnclickListener{
                    override fun onYesClick() {
                        val reagentId=dbManager!!.getReagentByPos("" + drawerID,"" + scApp?.touchtable).reagentId
                        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                        val now = sdf.format(Date())
                        val reagent = dbManager?.getReagentById(reagentId)
                        dbManager?.addSrapReagent(reagent?.reagentId,reagent?.reagentName,"","","",reagent!!.reagentType.toInt(),reagent?.reagentPurity,reagent?.reagentSize,reagent?.reagentTotalSize,reagent?.reagentCreater,"",reagent?.reagentUnit,reagent?.reagentDensity,reagent?.reagentInvalidDate,reagent?.reagentCabinetId,drawerID.toString(),reagent?.reagentPosition,4,scApp!!.userInfo.getUserName())
                        dbManager?.deleteReagentById(reagentId)
                        dbManager?.addReagentUserRecord(reagentId,4,now,scApp!!.userInfo.getUserName(),eT_weight?.text.toString(),"","")
                        val intent = Intent()
                        intent.setClass(applicationContext, OperationActivity::class.java)
                        startActivity(intent)
                        dialog.dismiss()
                    }
                })
            }
        }
        dialog.setNoOnclickListener("取消",object :OperationDialog.onNoOnclickListener{
            override fun onNoClick() {
                dialog.dismiss()
            }
        })
        dialog.show()
        dialog.window.setGravity(Gravity.CENTER)
    }
}
