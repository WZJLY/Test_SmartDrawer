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
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by WZJ on 2018/2/28.
 */

class DownloadThread(var uihandler: Handler) : Thread() {
    var downloadhandler: Handler? = null
    private var dbManager: DBManager? = null
    override fun run() {

        super.run()

        Looper.prepare()
//        dbManager = DBManager()
//        val path = "SmartCabinet/ReagentTemplate"
//        val fileName = "1422440131" + ".csv"
//        val SDCard = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + ""
//        val pathName = "$SDCard/$path/$fileName"
//        templateToDB(pathName)
//        downloadhandler = object : Handler() {
//            override fun handleMessage(msg: Message) {
//                super.handleMessage(msg)
//                if (msg.what == 2) {
//                    //使用主线程的handler发送消息到主线程的looper
//                    val path = "SmartCabinet/ReagentTemplate"
//                    val fileName =msg.obj.toString() + ".csv"
//                    val urlStr = SC_Const.REAGENTTEMPLATEADDRESS + fileName
//                    var output: OutputStream? = null
//                    val SDCard = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + ""
//                    val pathName = "$SDCard/$path/$fileName"
//                    val url = URL(urlStr)
//                    val conn = url.openConnection() as HttpURLConnection
//                    conn.requestMethod="GET"
//                    conn.useCaches=false
//                    conn.connectTimeout=10000
//                    conn.readTimeout=10000
//                    val file = File(pathName)
//                    val dir = SDCard + "/" + path
//                    File(dir).mkdir()//新建文件夹
//                    val input = conn.getInputStream()
//                    file.createNewFile()//新建文件
//                    if (file.exists())
//                        Log.d("file already exists:", pathName)
//                    else
//                        Log.d("file already noexists:", pathName)
//                    output = FileOutputStream(file)
//                    //读取大文件
//                    val buffer = ByteArray(4 * 1024)
//
//                    while (input.read(buffer) != -1) {
//                        output!!.write(buffer)
//                    }
//                    output!!.flush()
//                    output!!.close()
//                    val sendmsg = Message.obtain()
//                    sendmsg.what = 3
//                    uihandler.sendMessage(sendmsg)
//                }
//
//
//            }
//
//        }
//        val sendmsg = Message.obtain()
//        sendmsg.what = 1
//        sendmsg.obj = downloadhandler   //利用Message.obj把子线程的handle传递给主线程。
//        uihandler.sendMessage(sendmsg)

        Looper.loop()   //Looper好像一定要放在最后才有效。
    }
    fun templateToDB(filePath: String?): String {
        var ret = ""
        val content = "" //文件内容字符串
        //打开文件
        if (filePath == null)
            return "导入模板失败"
        val file = File(filePath)
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory) {
            Log.e(filePath, "The File doesn't not exist.")
            ret = "导入模板失败"
        } else {
            try {
                val instream = FileInputStream(file)
                if (instream != null) {
                    val inputreader = InputStreamReader(instream)
                    val buffreader = BufferedReader(inputreader)
                    var line:String = buffreader.readLine()
                    //分行读取
                    var lineNumber = 1
                    var lineArray: Array<String>? = null
                    dbManager?.deleteAllReagentTemplate()   //删除原有数据
                    while (line != null) {
                        if (lineNumber > 1) {     //insert to DB
                            lineArray = line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            if (lineArray[0] != null && lineArray[0] !== ""&&lineArray[0]!="试剂类型ID") {
                                if (lineArray[5] == "") lineArray[5] = "1"
                                dbManager?.addReagentTemplate(lineArray[0], lineArray[1], lineArray[2], lineArray[3], lineArray[4], 1,
                                        lineArray[6], lineArray[7], lineArray[8], lineArray[9], lineArray[10], lineArray[11] )
                            }
                        }
                        lineNumber++
                    }
                    instream.close()
                }
            } catch (e: Exception) {
                Log.e("TestFile", e.message)
                ret = "导入模板失败"
            }

        }

        return ret
    }
}

