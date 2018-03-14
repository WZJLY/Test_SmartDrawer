package com.example.smartcabinet

import android.content.DialogInterface
import android.content.Intent
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.SC_Const
import kotlinx.android.synthetic.main.activity_admin.*
import java.io.*
import java.net.MalformedURLException
import java.net.URL

class AdminActivity : AppCompatActivity(),AdminFragment.AdminFragmentListener,OrinaryFragment.orinarybuttonlisten,PersonLineFragment.deletbuttonlisten, AddPersonFragment.addpersonbuttonlisten, EditPersonFragment.savepersonbuttonlisten ,SetCabinetFragment.SetCabinetListener,SetDrawerFragment.SetDrawerFragmentListener,DrawerFragment1.deletDrawerFragmentListener{
    private var scApp: SCApp? = null
    private var returnview = "login"
    private var dbManager: DBManager? = null
    var handler =Handler()
    var download_handler=Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        dbManager = DBManager(applicationContext)
        scApp = application as SCApp
        val userAccount =  scApp?.getUserInfo()
        when(userAccount?.getUserPower()){
            SC_Const.ADMIN -> {
                val adminfrag = AdminFragment()
                replaceFragment(adminfrag, R.id.framelayout)
            }
            SC_Const.NORMAL -> {
                val orinaryFragment = OrinaryFragment()
                replaceFragment(orinaryFragment, R.id.framelayout)
            }
        }
        back_button.setOnClickListener({
            when(returnview){
                "admin" -> {
                    val adminfrag = AdminFragment()
                    replaceFragment(adminfrag, R.id.framelayout)
                    returnview ="login"
                }
                "orinary" -> {
                    val orinaryFragment = OrinaryFragment()
                    replaceFragment(orinaryFragment, R.id.framelayout)
                    returnview ="login"
                }
                "editperson"-> {
                    val editperson = AddPersonFragment()
                    replaceFragment(editperson, R.id.framelayout)
                    returnview ="admin"
                }
                "login" -> {
                    val intent =Intent()
                    intent.setClass(this,LoginActivity::class.java)
                    startActivity(intent)
                }
                "find_out"->{
                    val setCabinetFragment = SetCabinetFragment()
                    replaceFragment(setCabinetFragment,R.id.framelayout)
                    returnview = "admin"
                }
            }
        })
    }

    override  fun deletDrawerButtonClick(text: String,drawerID:Int) {
        if(text == "delet")
        {
            val setCabinet = SetCabinetFragment()
            replaceFragment(setCabinet, R.id.framelayout)
        }
        if(text == "find_out")
        {
            returnview = "find_out"
            val setDrawer = SetDrawerFragment()
            val args = Bundle()
            args.putString("setDrawer",""+drawerID)
            setDrawer.setArguments(args)
            replaceFragment(setDrawer, R.id.framelayout)


        }
    }
    override fun saveDrawerButtonClick(text: String) {
        if(text == "saveDrawer") {
            val setCabinet = SetCabinetFragment()
            replaceFragment(setCabinet, R.id.framelayout)
        }
    }
    override fun savepersonButtonClick(text: String) {
        if(text == "save") {
            val editperson = AddPersonFragment()
            replaceFragment(editperson, R.id.framelayout)
        }
    }
    override fun addpersonButtonClick(text: String) {
        returnview = "editperson"
        if (text == "addperson")
        {

            val editMessageFragment = EditPersonFragment()
            val args = Bundle()
            args.putString("editfile","addperson")
            editMessageFragment.setArguments(args)
            replaceFragment(editMessageFragment, R.id.framelayout)

        }
    }
    override fun deletButtonClick(text: String) {
        returnview = "admin"
        if(text == "delet") {
           val editperson = AddPersonFragment()
           replaceFragment(editperson, R.id.framelayout)
        }
    }
    override fun orinaryButtonClick(text: String)
    {
        returnview = "orinary"
        when(text){
            "regentsearch" -> {
                val serachfrag = SerachFragment()
                replaceFragment(serachfrag, R.id.framelayout)
            }
            "edit_flie" ->
            {

                val editMessageFragment = EditPersonFragment()
                val args = Bundle()
                args.putString("editfile","editperson")
                editMessageFragment.setArguments(args)
                replaceFragment(editMessageFragment, R.id.framelayout)
            }
            "reagent_operation" ->{
                val intent = Intent()
                intent.setClass(this,OperationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onButtonClick(text: String) {
        returnview = "admin"
        when(text) {
            "setBox_button" -> {
                val setCabinet = SetCabinetFragment()
                replaceFragment(setCabinet, R.id.framelayout)
            }
            "reagent_search"-> {
                val serachfrag = SerachFragment()
                replaceFragment(serachfrag, R.id.framelayout)
            }
            "personal_management"-> {
                val editperson = AddPersonFragment()
                replaceFragment(editperson, R.id.framelayout)
            }
            " editflie" ->
            {
                val editMessageFragment = EditPersonFragment()
                val args = Bundle()
                args.putString("editfile","editperson")
                editMessageFragment.setArguments(args)
                replaceFragment(editMessageFragment, R.id.framelayout)

            }
            "reagent_op"-> {
                val intent = Intent()
                intent.setClass(this,OperationActivity::class.java)
                startActivity(intent)
            }
            "recordquery" -> {

            }
            "auto_update" -> {

            }
            "reagent_template" -> {

                val builder = AlertDialog.Builder(this)
                builder.setTitle("提示")
                var edit :EditText= EditText(this)
                builder?.setView(edit)
                builder.setMessage("请输入试剂模板编号")
                builder.setPositiveButton("确定", DialogInterface.OnClickListener{ dialogInterface, i ->
                    scApp?.templateID=edit.text.toString()
//                    val status = Environment.getExternalStorageState()
//                    if (status.equals(Environment.MEDIA_MOUNTED)) {
//                        val main = Environment.getExternalStorageDirectory().getPath() + File.separator + "hello"
//                        val destDir = File(main)
//                        if (!destDir.exists()) {
//                            destDir.mkdirs()
//                        }
//                        destDir.mkdirs()
//                        val toFile = main + File.separator + "test.txt"
//                        FileOutputStream(toFile)//创建test.text空文件
//                    }
                    downLoad() //下载与导入模板的线程开启
                })
                builder.setNeutralButton("取消",null)
                builder.create()
                builder.show()

            }
        }
    }
    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }


    fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction{
            replace(frameId, fragment)
        }
    }

    override fun setCabinetClick(fragment: String) {
        when (fragment){
            "setDrawer" -> {

                val setDrawer = SetDrawerFragment()
                val args = Bundle()
                args.putString("setDrawer","set")
                setDrawer.setArguments(args)
                replaceFragment(setDrawer, R.id.framelayout)
                Log.d("data",fragment)

            }
            else -> {
                Log.d("data","else")
            }
        }
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
                    dbManager?.deleteAllReagentTemplate()   //删除原有数据
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

}




