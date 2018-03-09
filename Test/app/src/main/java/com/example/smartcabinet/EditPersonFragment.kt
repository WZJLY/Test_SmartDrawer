package com.example.smartcabinet

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
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
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.fragment_edit_person, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val Madapter = ArrayAdapter.createFromResource(context, R.array.power, R.layout.spinner_style)
        Madapter?.setDropDownViewResource(R.layout.dropdown_style)
        spinner_level.adapter = Madapter
        dbManager = DBManager(context.applicationContext)
        scApp = context.applicationContext as SCApp

        userID = scApp!!.getUserInfo().getUserId()
        username=dbManager!!.getUserAccountByUserId(userID).getUserName()
        userpasswd=dbManager!!.getUserAccountByUserId(userID).getUserPassword()
        if(getArguments().getString("editfile") =="editperson")
        {
            UserMessage()
        }

        val userAccount = UserAccount()
        spinner_level.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View,
                                        pos: Int, id: Long) {
                val power = resources.getStringArray(R.array.power)

                if(power[pos]=="管理员")
                {
                    userAccount.userPower = SC_Const.ADMIN
                }
                if(power[pos]=="普通")
                {
                    userAccount.userPower = SC_Const.NORMAL
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
        button_save.setOnClickListener {
            if(getArguments().getString("editfile") =="editperson")
            {
                updateUserAccountByID(userID)
                Toast.makeText(context,"个人信息修改成功",Toast.LENGTH_SHORT).show()
            }
            else {
                userAccount.userId = editText_Num.text.toString()
                userAccount.userName = editText_userName.text.toString()
                if (editText_Num.length() == 0) {
                    Toast.makeText(context, "编码信息未填写", Toast.LENGTH_SHORT).show()
                }
                if (editText_userName.length() == 0) {
                    Toast.makeText(context, "姓名未填写", Toast.LENGTH_SHORT).show()
                }
                if (editText_Password.length() == 0) {
                    Toast.makeText(context, "密码为空", Toast.LENGTH_SHORT).show()
                }
                if (editText_Password.text.toString() == editText_Password2.text.toString() && editText_Num.length() != 0 && editText_userName.length() != 0 && editText_Password.length() != 0) {
                    userAccount.userPassword = editText_Password.text.toString()
                    if (dbManager?.isAccountExist(userAccount.userName) == true) {
                        Toast.makeText(context, "该用户已经存在", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        dbManager?.addAccount(userAccount)
                        savebuttonClicked("save")
                        Toast.makeText(context, "用户添加成功", Toast.LENGTH_SHORT).show()
                    }
                } else
                    Toast.makeText(context, "两次密码输入不一样", Toast.LENGTH_SHORT).show()
            }
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
     *个人信息设置界面，无法修改编号，无法修改级别
     */
  private fun UserMessage(){
        textView_title.text ="个人信息修改"
        editText_Num.isEnabled = false
        editText_Num.isFocusable = false
        editText_Num.isFocusableInTouchMode = false
        textViewlevel.visibility = View.INVISIBLE
        spinner_level.visibility = View.INVISIBLE
        editText_userName.setText(username)
        editText_Password.setText(userpasswd)
        editText_Password2.setText(userpasswd)
        editText_Num.setText(userID)
    }
    /**
     *通过用户ID更新用户信息
     */
   private fun updateUserAccountByID(useid:String)
    {
        var usename = editText_userName.text.toString()
        var usepassword = editText_Password.text.toString()
        if(editText_Password2.text.toString()== editText_Password.text.toString())
        {
            dbManager?.updateAccountByUserId(useid,usename,usepassword,scApp!!.getUserInfo().getUserPower())
            editText_userName.setText(username)
            editText_Password.setText(usepassword)
            editText_Password2.setText(usepassword)
        }
        else
        {
            Toast.makeText(context.applicationContext,"两次密码输入不一样",Toast.LENGTH_SHORT).show()
        }

    }


}