package com.example.smartcabinet

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import com.example.smartcabinet.util.SC_Const
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity(),AdminFragment.AdminFragmentListener,OrinaryFragment.orinarybuttonlisten,PersonLineFragment.deletbuttonlisten,EditPersonFragment.addpersonbuttonlisten,EditMessageFragment.savepersonbuttonlisten ,SetCabinetFragment.SetCabinetListener,SetDrawerFragment.SetDrawerFragmentListener{
    private var scApp: SCApp? = null
    private var returnview = "login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        scApp = application as SCApp
      val userAccount =  scApp?.getUserInfo()
        when(userAccount?.getUserPower()){
            SC_Const.ADMIN ->
            {
                val adminfrag = AdminFragment()
                replaceFragment(adminfrag, R.id.framelayout)
            }
            SC_Const.NORMAL ->
            {
                val orinaryFragment = OrinaryFragment()
                replaceFragment(orinaryFragment, R.id.framelayout)
            }
        }
        back_button.setOnClickListener({
            when(returnview){
                "admin" ->
                {
                    val adminfrag = AdminFragment()
                    replaceFragment(adminfrag, R.id.framelayout)
                    returnview ="login"
                }
                "orinary" ->
                {
                    val orinaryFragment = OrinaryFragment()
                    replaceFragment(orinaryFragment, R.id.framelayout)
                    returnview ="login"
                }
                "editperson"->
                {
                    val editperson = EditPersonFragment()
                    replaceFragment(editperson, R.id.framelayout)
                    returnview ="admin"
                }
                "login" ->
                {
                    val intent =Intent()
                    intent.setClass(this,LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        })
    }

    override fun saveDrawerButtonClick(text: String) {
        if(text == "saveDrawer")
        {
            val setCabinet = SetCabinetFragment()
            replaceFragment(setCabinet, R.id.framelayout)
        }
    }
    override fun savepersonButtonClick(text: String) {
        if(text == "save")
        {
            val editperson = EditPersonFragment()
            replaceFragment(editperson, R.id.framelayout)
        }
    }
    override fun addpersonButtonClick(text: String) {
        returnview = "editperson"
        if (text == "addperson")
        {

            val editMessageFragment = EditMessageFragment()
            val args = Bundle()
            args.putString("editfile","addperson")
            editMessageFragment.setArguments(args)
            replaceFragment(editMessageFragment, R.id.framelayout)

        }
    }
    override fun deletButtonClick(text: String) {
        returnview = "admin"
       if(text == "delet")
       {
           val editperson = EditPersonFragment()
           replaceFragment(editperson, R.id.framelayout)
       }
    }
    override fun orinaryButtonClick(text: String)
    {
        returnview = "orinary"
        when(text){
            "regentsearch" ->
            {
                val serachfrag = SerachFragment()
                replaceFragment(serachfrag, R.id.framelayout)
            }
            "edit_flie" ->
            {

                val editMessageFragment = EditMessageFragment()
                val args = Bundle()
                args.putString("editfile","editperson")
                editMessageFragment.setArguments(args)
                replaceFragment(editMessageFragment, R.id.framelayout)
            }
        }
    }

    override fun onButtonClick(text: String) {
        returnview = "admin"
        when(text) {
            "setBox_button" ->
            {
                val setCabinet = SetCabinetFragment()
                replaceFragment(setCabinet, R.id.framelayout)
            }
            "reagent_search"->
            {
                val serachfrag = SerachFragment()
                replaceFragment(serachfrag, R.id.framelayout)
            }
            "personal_management"->
            {
                val editperson = EditPersonFragment()
                replaceFragment(editperson, R.id.framelayout)
            }
            " editflie" ->
            {
                val editMessageFragment =EditMessageFragment()
                val args = Bundle()
                args.putString("editfile","editperson")
                editMessageFragment.setArguments(args)
                replaceFragment(editMessageFragment, R.id.framelayout)

            }
            "reagent_op"->
            {
                val intent = Intent()
                intent.setClass(this,OperationActivity::class.java)
                startActivity(intent)
            }
            "recordquery" ->
            {

            }
            "auto_update" ->
            {

            }
            "reagent_templatep" ->
            {

            }
        }
    }
    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }


    fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction{replace(frameId, fragment)}
    }

    override fun setCabinetClick(fragment: String) {
        when (fragment){
            "setDrawer" -> {
                val setDrawer = SetDrawerFragment()
                replaceFragment(setDrawer, R.id.framelayout)
                Log.d("data",fragment)
            }
            else -> {
                Log.d("data","else")
            }
        }
    }
}




