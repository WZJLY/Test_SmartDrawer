package com.example.smartcabinet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.webkit.JavascriptInterface
import android.widget.EditText
import android.widget.Toast
import com.example.smartcabinet.util.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.dialog_binding.*
import kotlinx.android.synthetic.main.fragment_capture.*
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity :BaseActivity() {
    private var dbManager: DBManager? = null
    private var scApp: SCApp? = null
    private val LOGINNAME = "smart_cabinet)smart_cabinet_login_name"
    private val NAME = "name"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE
        dbManager = DBManager(this)
        dbManager?.tableUpgrade()
        scApp = application as SCApp
        if(dbManager!!.cabinetNo.size==0)
        {
            val dialog = BindingDialog(this)
            dialog.setYesOnclickListener(object :BindingDialog.onYesOnclickListener{
                override fun onYesClick() {
                    val  editText1 = dialog.findViewById(R.id.et_Dbinding_number) as EditText
                    val  editText2 =dialog.findViewById(R.id.et_Dbinding_serviceCode) as EditText
                    if(editText1.text.toString()!="") {
                        dbManager?.addCabinetNo(editText1.text.toString(), editText2.text.toString())
                        dialog.dismiss()
                    }
                    else
                        Toast.makeText(this@LoginActivity,"智能柜编码不能为空",Toast.LENGTH_SHORT).show()

                }



            })
            dialog.show()
        }

        val lastname = getLastLoginName()
        et_userName.setText(lastname)
        button_login.setOnClickListener({

            login(et_userName.text.toString(),et_userPassword.text.toString())
        })
        login_iV_passward.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == KeyEvent.ACTION_UP){
                et_userPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                et_userPassword.setSelection(et_userPassword.length())
            }
            else {
                et_userPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                et_userPassword.setSelection(et_userPassword.length())
            }
            true
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(null != this.currentFocus) {
            val mInputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0)
        }
        return super.onTouchEvent(event)
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
                val account = UserAccount(strUserId, strAccount, strPassword, iPower,"","","0")
                dbManager?.addAccount(account)
                scApp?.setUserInfo(strUserId, strAccount, strPassword, iPower,"","","0")
                saveUserName(strAccount)
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val curDate = Date(System.currentTimeMillis())
                val str = formatter.format(curDate)
                val upload :UploadRecordManager= UploadRecordManager(this)
                upload.getCode(dbManager!!.cabinetNo.get(0).cabinetNo,"登陆",scApp!!.userInfo.userName,str,"")

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


            if(userInfo?.statue!="1") {
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val curDate = Date(System.currentTimeMillis())
                val str = formatter.format(curDate)
                val upload :UploadRecordManager= UploadRecordManager(this)
                upload.getCode(dbManager!!.cabinetNo.get(0).cabinetNo,"登陆",scApp!!.userInfo.userName,str,"")
                intent.setClass(this, AdminActivity::class.java)
                intent.putExtra("SC_Const", 1)
                saveUserName(userName)
                startActivity(intent)
            }
            else
            Toast.makeText(this,"该用户已被禁用",Toast.LENGTH_SHORT).show()
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

}
