package com.example.smartcabinet

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.SC_Const
import com.example.smartcabinet.util.UserAccount
import kotlinx.android.synthetic.main.fragment_edit_person.*




/**
 * Created by WZJ on 2018/2/6.
 */
class EditPersonFragment : Fragment() {
    var activityCallback: EditPersonFragment.savepersonbuttonlisten? = null
    private var dbManager: DBManager? = null
    private var scApp: SCApp? = null
    private  var username = String()
    private var userpasswd = String()
    private var userID = String()
    private var userAccount= String()
    private var phoneNum= String()
    private var statue=String()
    private var userpower= 0
    private var user:UserAccount?=null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.fragment_edit_person, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val Madapter = ArrayAdapter.createFromResource(context, R.array.power, R.layout.spinner_style)
        Madapter?.setDropDownViewResource(R.layout.dropdown_style)
        spinner_level.adapter = Madapter
        dbManager = DBManager(context.applicationContext)
        scApp = context.applicationContext as SCApp
        activity.title = "新增人员"
        btn_enable.visibility = View.GONE
        if(arguments.getString("editfile") =="editperson")
        {
            activity.title = "个人信息修改"
            username=scApp!!.userInfo.getUserName()
            user=dbManager?.getUserAccountByUserName(username)
            userAccount=user?.getUserAccount().toString()
            userID=user!!.userId
            userpasswd=user!!.getUserPassword()
            phoneNum=user?.getPhoneNumber().toString()
            userpower=user!!.getUserPower()
            UserMessage()
        }
        if(arguments.getString("edit") =="editOther")
        {
            activity.title = "个人信息修改"
            username=scApp!!.editPerson
            user=dbManager?.getUserAccountByUserName(username)
            userAccount=user?.getUserAccount().toString()
            userID=user!!.userId
            userpasswd=user!!.getUserPassword()
            phoneNum=user?.getPhoneNumber().toString()
            userpower=user!!.getUserPower()
            statue=user?.getStatue().toString()
            UserMessage()
            btn_enable.visibility = View.VISIBLE
        }
        var userAccount=UserAccount()
        spinner_level.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View,
                                        pos: Int, id: Long) {
                val power = resources.getStringArray(R.array.power)

                if(power[pos]=="管理员")
                {
                    userAccount?.userPower = SC_Const.ADMIN
                    userpower=0
                }
                if(power[pos]=="普通")
                {
                    userAccount?.userPower = SC_Const.NORMAL
                    userpower=1
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                userpower=1
                // Another interface callback
            }
        }
        button_save.setOnClickListener {
//                if (editText_Num.length() == 0) {
//                    Toast.makeText(context, "编码信息未填写", Toast.LENGTH_SHORT).show()
//                }
                if (editText_userName.length() == 0) {
                    Toast.makeText(context, "姓名未填写", Toast.LENGTH_SHORT).show()
                }
                if (editText_Password.length() == 0) {
                    Toast.makeText(context, "密码为空", Toast.LENGTH_SHORT).show()
                }
//                if(editText_phoneNum.length()==0){
//                    Toast.makeText(context,"手机号未填写",Toast.LENGTH_SHORT).show()
//                }
                if(editText_account.length()==0)
                {
                    Toast.makeText(context, "账号未填写", Toast.LENGTH_SHORT).show()
                }
                if (editText_Password.text.toString() == editText_Password2.text.toString() && editText_userName.length() != 0 && editText_Password.length() != 0&&editText_account.length()!=0) {
                    if(arguments.getString("editfile") =="editperson")
                    {
                        updateUserAccountByName(username)
                        Toast.makeText(context,"个人信息修改成功",Toast.LENGTH_SHORT).show()
                    }
                    else {
                        userAccount.userPassword = editText_Password.text.toString()
                        userAccount.userId = editText_Num.text.toString()
                        userAccount.userName = editText_account.text.toString()
                        userAccount.phoneNumber = editText_phoneNum.text.toString()
                        userAccount.userAccount = editText_userName.text.toString()
                        userAccount.statue = "0"
                        if (dbManager?.isAccountExist(editText_account.text.toString()) == true) {
                            if(arguments.getString("edit") =="editOther")
                            {
                                updateUserAccountByName(username)
                                Toast.makeText(context, "信息修改成功", Toast.LENGTH_SHORT).show()
                                savebuttonClicked("save")
                            }
                            else
                            Toast.makeText(context, "该用户已经存在", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            dbManager?.addAccount(userAccount)
                            savebuttonClicked("save")
                            Toast.makeText(context, "用户添加成功", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else
                    Toast.makeText(context, "两次密码输入不一样", Toast.LENGTH_SHORT).show()
        }
        btn_enable.setOnClickListener{
            //判断该用户状态，如果为”0“则禁用，如果为”1“则启用
            if(statue=="0"||statue=="null")
                dbManager?.updateStatueByUserName(username,"1")
            if(statue=="1")
                dbManager?.updateStatueByUserName(username,"0")
            savebuttonClicked("save")
        }
    }
    interface savepersonbuttonlisten {
        fun savepersonButtonClick(text: String)
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as savepersonbuttonlisten
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }
    }
    private fun savebuttonClicked(text: String) {
        activityCallback?.savepersonButtonClick(text)
    }

    /**
     *人员设置界面，无法修改编号
     */
    fun PersonMessage(){
        textView_title.text = "人员设置"
        editText_Num.isEnabled = false
        editText_Num.isFocusable = false
        editText_Num.isFocusableInTouchMode = false
    }

    /**
     *个人信息设置界面，无法修改账号
     */
  private fun UserMessage(){
        if(statue=="1"){
            btn_enable.text="启用"
        }
        textView_title.text ="个人信息修改"
        editText_account.isEnabled = false
        editText_account.isFocusable = false
        editText_account.isFocusableInTouchMode = false
        if(userpower==0)
            spinner_level.setSelection(1)
        else
            spinner_level.setSelection(0)
        if(userAccount == "null")
            userAccount = ""
        editText_userName.setText(userAccount)
        editText_Password.setText(userpasswd)
        editText_Password2.setText(userpasswd)
        if(userID=="null")
            userID=""
        editText_Num.setText(userID)
        if(phoneNum == "null")
            phoneNum = ""
        editText_phoneNum.setText(phoneNum)
        editText_account.setText(username)
    }
    /**
     *通过用户ID更新用户信息
     */
   private fun updateUserAccountByName(userName:String)
    {
            dbManager?.updateAccountByUserName(userName,editText_Num.text.toString(),editText_Password.text.toString(),userpower,editText_userName.text.toString(),editText_phoneNum.text.toString())

    }


}