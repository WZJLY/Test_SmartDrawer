package com.example.smartcabinet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartcabinet.util.DBManager

import kotlinx.android.synthetic.main.fragment_line_person.*
import android.content.DialogInterface
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_edit_person.*


/**
 * Created by WZJ on 2018/2/7.
 */
class PersonLineFragment : Fragment() {
    var activityCallback: PersonLineFragment.deletbuttonlisten? = null
    private var dbManager: DBManager? = null
    private var scApp: SCApp? = null
    var userName = String()
    var statue = String()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if(arguments!=null)
        {
            userName = arguments.getString("userName")
            statue = arguments.getString("statue")
        }
        return inflater!!.inflate(R.layout.fragment_line_person, container, false)
    }
    interface deletbuttonlisten {
        fun deletButtonClick(text: String)
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as deletbuttonlisten
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }

    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        scApp = context.applicationContext as SCApp
        dbManager = DBManager(context.applicationContext)
        val daccout=dbManager?.getUserAccountByUserName(userName)
        tV_userName.text=daccout?.getUserName()
        val account= scApp?.userInfo
        if (statue == "1")
            iBt_person.setBackgroundResource(R.drawable.un_line_person)
        if(daccout?.userPower==0)
            imageView2.setBackgroundResource(R.drawable.admin)
        if(userName=="admin") {
            iBt_deletPerson.visibility = View.GONE   //对可删除的用户就行了判断
            // 管理级别为最高，接下去普通管理员，然后是普通用户
            //同级之间无法删除，只能上级删除下级
        }
        else{
            if(account?.userPower==0&&account.userName!="admin") {
                iBt_deletPerson.visibility = View.GONE
            }
        }

        iBt_deletPerson.setOnClickListener({            //用户的删除
            val builder = AlertDialog.Builder(context)
            builder.setTitle("提示")
            builder.setMessage("是否删除该用户")
            builder.setPositiveButton("确定", DialogInterface.OnClickListener{ dialogInterface, i ->
                val usename = tV_userName.text.toString()
                dbManager?.deleteAccountByUserName(usename)
                deletbuttonClicked("delet")
                Toast.makeText(context.applicationContext, "删除成功", Toast.LENGTH_SHORT).show()
            })
            builder.setNeutralButton("取消",null)
            builder.create()
            builder.show()
        })

        iBt_person.setOnClickListener{
            if(account?.userName=="admin") {
                scApp?.editPerson=userName
                deletbuttonClicked("edit")
            }
            else{
                if(account?.userPower==0){
                    if(userName!="admin"&&daccout?.userPower!=0) {
                        scApp?.editPerson = userName
                        deletbuttonClicked("edit")
                    }
                }
            }
        }
    }
    private fun deletbuttonClicked(text: String) {
        activityCallback?.deletButtonClick(text)
    }



}
