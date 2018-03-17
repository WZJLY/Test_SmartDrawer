package com.example.smartcabinet


import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import kotlinx.android.synthetic.main.dialog_operation.*
import android.content.Context
import android.graphics.Color
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView


/**
 * A simple [Fragment] subclass.
 */
class OperationDialog(context: Context) : Dialog(context){

    private var yesStr: String? = null
    private var noStr: String? = null
    private var titleStr: String? = null//从外界设置的title文本
    private var messageStr: String? = null//从外界设置的消息文本

    private var noOnclickListener: onNoOnclickListener? = null//取消按钮被点击了的监听器
    private var yesOnclickListener: onYesOnclickListener? = null//确定按钮被点击了的监听器

    fun setNoOnclickListener(str: String?, onNoOnclickListener: onNoOnclickListener) {
        if (str != null) {
            noStr = str
        }
        this.noOnclickListener = onNoOnclickListener
    }

    fun setYesOnclickListener(str: String?, onYesOnclickListener: onYesOnclickListener) {
        if (str != null) {
            yesStr = str
        }
        this.yesOnclickListener = onYesOnclickListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_operation)
        setCanceledOnTouchOutside(false)

        initData()
        initEvent()
    }

    private fun initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        dialog_btn_yes.setOnClickListener {
            if (yesOnclickListener != null) {
                yesOnclickListener!!.onYesClick()
            }
        }

        dialog_btn_no.setOnClickListener {
            if (noOnclickListener != null) {
                noOnclickListener!!.onNoClick()
            }
        }
    }

    private fun initData() {
        //如果用户自定了title和message
        if (titleStr != null) {
            dialog_title.text = titleStr
        }
        if (messageStr != null) {
            dialog_message.text = messageStr
        }
        //如果设置按钮的文字
        if (yesStr != null) {
            dialog_btn_yes.text = yesStr
        }
        if (noStr != null) {
            dialog_btn_no.text = noStr
        }
    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    fun setTitle(title: String) {
        titleStr = title
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    fun setMessage(message: String) {
        messageStr = message
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    interface onYesOnclickListener {
        fun onYesClick()
    }

    interface onNoOnclickListener {
        fun onNoClick()
    }

    fun addNum2(num: Int){
        for(i in 1..num){
            val tableRow = TableRow(context)
            for(j in 1..num){
                val textView = TextView(context)
                textView.width = 40
                textView.height = 40
                textView.setBackgroundResource(R.drawable.btn_table)

                tableRow.addView(textView)
            }
            tableLayout2.addView(tableRow)
        }
    }
}
