package com.example.smartcabinet


import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.ReagentTemplate
import com.example.smartcabinet.util.SC_Const
import kotlinx.android.synthetic.main.fragment_edit_template.*
import java.io.*
import java.net.MalformedURLException
import java.net.URL


/**
 * A simple [Fragment] subclass.
 */
class EditTemplateFragment : Fragment() {
    var activityCallback: EditTemplateFragment.editTemplateListen? = null
    private var reagentTemplate: ReagentTemplate?=null
    private var dbManager: DBManager? = null
    private var scApp: SCApp? = null
    interface editTemplateListen {
        fun editTemplateButtonClick(text: String)
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_edit_template, container, false)
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        scApp = context.applicationContext as SCApp
        dbManager = DBManager(context)
        val arrListReagentTemplate = dbManager?.reagentTemplate
        val sum = arrListReagentTemplate!!.size
        if(sum>0)
        {
            for (i in 1..sum) {

                reagentTemplate = arrListReagentTemplate?.get(i - 1)
                val fragment = childFragmentManager.beginTransaction()
                val templateLineFragment = TemplateLineFragment()
                val args = Bundle()
                args.putInt("order",i-1)
                templateLineFragment.arguments = args
                fragment.add(R.id.Linear_editTemplate,templateLineFragment)
                fragment.commit()
            }
        }
        else
            Toast.makeText(context,"没有试剂模板",Toast.LENGTH_SHORT).show()
        btn_import.setOnClickListener{

            editTemplateClicked("btn_import")
        }
        btn_clean.setOnClickListener{
            editTemplateClicked("btn_clean")

        }
        btn_single.setOnClickListener{
            editTemplateClicked("btn_single")
        }
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as editTemplateListen
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }
    }

    private fun editTemplateClicked(text: String) {
        activityCallback?.editTemplateButtonClick(text)
    }

    fun templateToDB(filePath: String?): String {
        var ret = ""
        var content = "" //文件内容字符串
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
                    var line =buffreader.readLine()
                    var lineNumber = 1
                    var lineArray: Array<String>? = null
//                    dbManager?.deleteAllReagentTemplate()   //删除原有数据
                    while ( line != null) {
                        line=buffreader.readLine()
                        Log.e("wzj",line)
                        if (lineNumber >= 1) {     //insert to DB
                            lineArray = line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            if (lineArray[0] != null && lineArray[0] !== "") {
                                if (lineArray[5] == "") lineArray[5] = "1"
                                dbManager?.addReagentTemplate(lineArray[0], lineArray[1], lineArray[2], lineArray[3], lineArray[4], lineArray[5].toInt(),
                                        lineArray[6], lineArray[7], lineArray[8], lineArray[9], lineArray[10], lineArray[11])
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



    fun downLoad() {
        object : Thread() {
            override fun run() {
                val path = "SmartCabinet/ReagentTemplate"
                val fileName = scApp?.templateID + ".csv"
                val urlStr = SC_Const.REAGENTTEMPLATEADDRESS + fileName
                val SDCard = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + ""
                val pathName = "$SDCard/$path/$fileName"//文件存储路径
                try {
                    /*
                    * 通过URL取得HttpURLConnection
                    * 要网络连接成功，需在AndroidMainfest.xml中进行权限配置
                    * <uses-permission android:name="android.permission.INTERNET" />
                    */
                    val url = URL(urlStr)
                    //取得inputStream，并将流中的信息写入SDCard

                    /*
                         * 写前准备
                         * 1.在AndroidMainfest.xml中进行权限配置
                         * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
                         * 取得写入SDCard的权限
                         * 2.取得SDCard的路径： Environment.getExternalStorageDirectory()
                         * 3.检查要保存的文件上是否已经存在
                         * 4.不存在，新建文件夹，新建文件
                         * 5.将input流中的信息写入SDCard
                         * 6.关闭流
                         */
                    val status = Environment.getExternalStorageState()
                    if (status.equals(Environment.MEDIA_MOUNTED)) {
                        val main = Environment.getExternalStorageDirectory().getPath() + File.separator + "SmartCabinet/ReagentTemplate"
                        val destDir = File(main)
                        if (!destDir.exists()) {
                            destDir.mkdirs()
                        }
                        destDir.mkdirs()
                    }
                    Looper.prepare()

                    Toast.makeText(SCApp.getContext(), "开始下载试剂模板", Toast.LENGTH_LONG).show()
                    val dir = SDCard + "/" + path
                    File(dir).mkdir()//新建文件夹
                    val output = File(pathName)
                    val requestUrl = URL(urlStr)
                    output.writeBytes(requestUrl.readBytes())
                    Toast.makeText(SCApp.getContext(), "模板下载成功", Toast.LENGTH_SHORT).show()


                    if (templateToDB(pathName) == "") {

                        Toast.makeText(SCApp.getContext(), "试剂模板导入失败", Toast.LENGTH_SHORT).show()

                    }
                    else {

                        Toast.makeText(SCApp.getContext(), "试剂模板导入成功", Toast.LENGTH_SHORT).show()

                    }
                } catch (e: MalformedURLException) {

                    e.printStackTrace()

                } catch (e: IOException) {

                    Toast.makeText(SCApp.getContext(), "该试剂模板编码不存在", Toast.LENGTH_LONG).show()

                    e.printStackTrace()

                } finally {
                    try {

                    } catch (e: IOException) {

                        e.printStackTrace()

                    }
                    Looper.loop()
                }

            }
        }.start()  //开启一个线程
    }
}// Required empty public constructor
