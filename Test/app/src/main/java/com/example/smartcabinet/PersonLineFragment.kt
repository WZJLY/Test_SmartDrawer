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


/**
 * Created by WZJ on 2018/2/7.
 */
class PersonLineFragment : Fragment() {
    private var dbManager: DBManager? = null
    var text = String()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if(getArguments()!=null)
        {
            text = getArguments().getString("username")

        }
        return inflater!!.inflate(R.layout.fragment_line_person, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager = DBManager(context.applicationContext)
        tV_userName.text=text
        iBt_deletPerson.setOnClickListener({
            val builder = AlertDialog.Builder(context)
            builder.setTitle("提示")
            builder.setMessage("是否删除该用户")
            builder.setPositiveButton("确定", DialogInterface.OnClickListener{ dialogInterface, i ->
                val args = Bundle()
                args.putString("rmUsername",text)
                val editPerson = EditPersonFragment()
                editPerson.setArguments(args)
                val usename = tV_userName.text.toString()
                dbManager?.deleteAccountByUserName(usename)
          val intent = Intent()
            intent.setClass(context.applicationContext,EditPersonActivity::class.java)
            startActivity(intent)
                Toast.makeText(context.applicationContext, "删除成功", Toast.LENGTH_SHORT).show()
            })
            builder.setNeutralButton("取消",null)
            builder.create()
            builder.show()

        })
    }



}
