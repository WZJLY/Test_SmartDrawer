package com.example.smartcabinet

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.widget.Toast
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.SC_Const
import com.example.smartcabinet.util.UserAccount
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private var dbManager: DBManager? = null
    private var scApp: SCApp? = null
    private val LOGINNAME = "smart_cabinet)smart_cabinet_login_name"
    private val NAME = "name"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE
        dbManager = DBManager(this)
        dbManager?.tableUpgrade()
        scApp = application as SCApp

        val lastname = getLastLoginName()
        et_userName.setText(lastname)
        button_login.setOnClickListener({
            login(et_userName.text.toString(),et_userPassword.text.toString())
        })

    }

    @JavascriptInterface
    fun getLastLoginName(): String {
        val sharedPreferences = this.getSharedPreferences(LOGINNAME, MODE_PRIVATE)
        return sharedPreferences.getString(NAME, "")!! + ""
    }

    @JavascriptInterface
    fun closeAPP() {
        finish()
    }

    fun saveUserName(strUserName: String) {
        val sharedPreferences = getSharedPreferences(LOGINNAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(NAME, strUserName)
        editor.commit()
    }

    fun login(userName: String, userPWD: String) {
        val intent = Intent()
        if (userName == "admin" && !dbManager!!.isAccountExist(userName)) {
            //first time login with admin - admin/admin
            if (userPWD == "admin") {
                val strUserId = "00001"
                val strAccount = "admin"
                val strPassword = "admin"
                val iPower = SC_Const.ADMIN
                val account = UserAccount(strUserId, strAccount, strPassword, iPower)
                dbManager?.addAccount(account)
                scApp?.setUserInfo(strUserId, strAccount, strPassword, iPower)
                saveUserName(strAccount)
                intent.setClass(this, AdminActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this.applicationContext,"登陆失败", Toast.LENGTH_SHORT).show()
                return
            }
        }
        if (!dbManager!!.isAccountExist(userName, userPWD)) {
            Toast.makeText(this.applicationContext, "登陆失败", Toast.LENGTH_SHORT).show()
        } else {
            val userInfo = dbManager?.getUserAccount(userName, userPWD)
            scApp?.userInfo = userInfo

            //upload user login record to server



                intent.setClass(this, AdminActivity::class.java)
                intent.putExtra("SC_Const",1)

            saveUserName(userName)
            startActivity(intent)
//            finish()
        }
    }


    override fun onResume() {
        super.onResume()
        et_userPassword.text = null

    }

    override fun onDestroy() {
        super.onDestroy()
        dbManager?.closeDB()
    }

//    private fun closeBar() {
//        try {
//            val command: String
//            command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib service call activity 42 s16 com.android.systemui"
//            val envlist = ArrayList<String>()
//            val env = System.getenv()
//            for (envName in env.keys) {
//                envlist.add(envName + "=" + env[envName])
//            }
//            val envp = envlist.toArray(arrayOfNulls<String>(0))
//            val proc = Runtime.getRuntime().exec(
//                    arrayOf("su", "-c", command), envp)
//            proc.waitFor()
//        } catch (ex: Exception) {
//            // Toast.makeText(getApplicationContext(), ex.getMessage(),
//            // Toast.LENGTH_LONG).show();
//        }
//
//    }
//
//    fun showBar() {
//        try {
//            val command: String
//            command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib am startservice -n com.android.systemui/.SystemUIService"
//            val envlist = ArrayList<String>()
//            val env = System.getenv()
//            for (envName in env.keys) {
//                envlist.add(envName + "=" + env[envName])
//            }
//            val envp = envlist.toArray(arrayOfNulls<String>(0))
//            val proc = Runtime.getRuntime().exec(
//                    arrayOf("su", "-c", command), envp)
//            proc.waitFor()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//    }
}
