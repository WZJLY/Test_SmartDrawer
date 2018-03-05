package com.example.smartcabinet

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.ReagentTemplate
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_sub_operation.*
import kotlinx.android.synthetic.main.fragment_information1.*
import kotlinx.android.synthetic.main.fragment_information2.*
import kotlinx.android.synthetic.main.fragment_line_person.*

class SubOperationActivity : AppCompatActivity(),InformationFragment2.scanbuttonlisten,InformationFragment1.return_scanbuttonlisten{
private var statue:String?=null
    private var scApp: SCApp? = null
    private var dbManager:DBManager?=null
    private var reagentTemplate:ReagentTemplate?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_operation)
        scApp = application as SCApp
        dbManager = DBManager(applicationContext)
        val subOperation: String = intent.getStringExtra("subOperation")
        val scan_value = intent.getStringExtra("scan_value")
        val spi =  scApp?.getSpi()

        when(subOperation) {
            "Into" -> {
                val tableFragment = TableFragment()
                val arg = Bundle()
                arg.putString("statue","op")
                arg.putInt("tablenum_op", scApp!!.getTouchdrawer())
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
            when(subOperation)
            {
                "Into" -> {



                    //开锁
                    spi?.sendOpenLock(1,scApp!!.getTouchdrawer())
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("入柜")
                    builder.setMessage("请放入试剂后点击确定")
                    builder.setPositiveButton("确定", DialogInterface.OnClickListener{ dialogInterface, i ->
                        reagentTemplate =  dbManager!!.reagentTemplate.get(scApp!!.templateNum)
                        dbManager?.addReagent(eT_code.text.toString(),reagentTemplate?.reagentName,"",""
                                ,"",1,reagentTemplate?.reagentPurity,reagentTemplate?.reagentSize, eT_weight2.text.toString()
                                ,reagentTemplate?.reagentCreater,reagentTemplate?.reagentGoodsID,1,reagentTemplate?.reagentDensity,eT_data.text.toString()
                                ,"1",scApp?.getTouchdrawer().toString(),scApp?.getTouchtable().toString(),1,scApp!!.userInfo.getUserName())

                    val intent =Intent()
                    scApp?.setTouchtable(0)
                    scApp?.setTouchdrawer(0) //新加的
                    intent.setClass(this,OperationActivity::class.java)
                    startActivity(intent)
                    })
                    builder.setNeutralButton("取消",null)
                    builder.create()
                    builder.show()

                }

                "Take" -> {

                    //开锁
                    spi?.sendOpenLock(1,scApp!!.getTouchdrawer())
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("取用")
                    builder.setMessage("请取出试剂后点击确定")
                    builder.setPositiveButton("确定", DialogInterface.OnClickListener{ dialogInterface, i ->
                        dbManager?.updateReagentStatusByPos(""+scApp?.getTouchdrawer(),""+scApp?.getTouchtable(),scApp!!.getUserInfo().getUserName(),2)

                        val intent =Intent()
                        intent.setClass(this,OperationActivity::class.java)
                        startActivity(intent)
                    })
                    builder.setNeutralButton("取消",null)
                    builder.create()
                    builder.show()
                }

                "Return" -> {

                    if(dbManager?.isReagentExist(eT_code2.text.toString())==true)
                    {
                        if(dbManager!!.getReagentById(eT_code2.text.toString()).status==2)
                        {
                            if(eT_weight.text!=null) {
                                //开锁
                                spi?.sendOpenLock(1,scApp!!.getTouchdrawer())
                                val builder = AlertDialog.Builder(this)
                                builder.setTitle("归还")
                                builder.setMessage("请归还试剂后点击确定")
                                builder.setPositiveButton("确定", DialogInterface.OnClickListener{ dialogInterface, i ->
                                    dbManager?.updateReagentStatus(eT_code2.text.toString(), 1, scApp!!.userInfo.getUserName())
                                    dbManager?.updateReagentSize(eT_code2.text.toString(), eT_weight.text.toString())

                                    val intent = Intent()
                                    intent.setClass(this, OperationActivity::class.java)
                                    startActivity(intent)
                                })
                                builder.setNeutralButton("取消",null)
                                builder.create()
                                builder.show()
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

                    //开锁
                    spi?.sendOpenLock(1,scApp!!.getTouchdrawer())

                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("报废")
                    builder.setMessage("请取出试剂后点击确定")
                    builder.setPositiveButton("确定", DialogInterface.OnClickListener{ dialogInterface, i ->
                        dbManager?.deleteReagentByPos(""+scApp?.getTouchdrawer(),""+scApp?.getTouchtable())

                        val intent =Intent()
                        intent.setClass(this,OperationActivity::class.java)
                        startActivity(intent)
                    })
                    builder.setNeutralButton("取消",null)
                    builder.create()
                    builder.show()


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
    }

    override fun return_scanbuttononClick(text: String) {
        if(text == "scan")
        {
            statue="Return"
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

}
