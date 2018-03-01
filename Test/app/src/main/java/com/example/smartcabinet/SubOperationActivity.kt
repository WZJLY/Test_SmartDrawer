package com.example.smartcabinet

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_sub_operation.*
import kotlinx.android.synthetic.main.fragment_information1.*
import kotlinx.android.synthetic.main.fragment_information2.*

class SubOperationActivity : AppCompatActivity(),InformationFragment2.scanbuttonlisten,InformationFragment1.return_scanbuttonlisten{
private var statue:String?=null
    private var scApp: SCApp? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_operation)
        scApp = application as SCApp
        val subOperation: String = intent.getStringExtra("subOperation")
        val scan_value = intent.getStringExtra("scan_value")
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
