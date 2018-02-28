package com.example.smartcabinet

import android.app.Application
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.preference.PreferenceActivity
import android.util.Log
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.SC_Const
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by WZJ on 2018/2/28.
 */

class DownloadThread(internal var uihandler: Handler) : Thread() {
    var downloadhandler: Handler? = null

    override fun run() {
        super.run()
        Looper.prepare()
        downloadhandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == 2) {
                    //使用主线程的handler发送消息到主线程的looper
                    val path = "SmartCabinet/ReagentTemplate"
                    val fileName =msg.obj.toString() + ".csv"
                    val urlStr = SC_Const.REAGENTTEMPLATEADDRESS + fileName
                    var output: OutputStream? = null
                    val SDCard = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + ""
                    val pathName = "$SDCard/$path/$fileName"
                    val url = URL(urlStr)
                    val conn = url.openConnection() as HttpURLConnection
                    conn.requestMethod="GET"
                    conn.useCaches=false
                    conn.connectTimeout=10000
                    conn.readTimeout=10000
                    val file = File(pathName)
                    val dir = SDCard + "/" + path
                    File(dir).mkdir()//新建文件夹
                    val input = conn.getInputStream()
                    file.createNewFile()//新建文件
                    if (file.exists())
                        Log.d("file already exists:", pathName)
                    else
                        Log.d("file already noexists:", pathName)
                    output = FileOutputStream(file)
                    //读取大文件
                    val buffer = ByteArray(4 * 1024)

                    while (input.read(buffer) != -1) {
                        output!!.write(buffer)
                    }
                    output!!.flush()
                }


            }

        }
        val sendmsg = Message.obtain()
        sendmsg.what = 1
        sendmsg.obj = downloadhandler   //利用Message.obj把子线程的handle传递给主线程。
        uihandler.sendMessage(sendmsg)

        Looper.loop()   //Looper好像一定要放在最后才有效。
    }

}
