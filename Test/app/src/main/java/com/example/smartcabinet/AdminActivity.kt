package com.example.smartcabinet

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.EditText
import android.widget.Toast
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.SC_Const
import kotlinx.android.synthetic.main.activity_admin.*
import java.io.*
import java.net.MalformedURLException
import java.net.URL

import android.view.inputmethod.InputMethodManager
import com.example.smartcabinet.util.UpdateAppManager
import java.util.*


class AdminActivity : BaseActivity(),AdminFragment.AdminFragmentListener,OrinaryFragment.orinarybuttonlisten,PersonLineFragment.deletbuttonlisten, AddPersonFragment.addpersonbuttonlisten,
        EditPersonFragment.savepersonbuttonlisten ,SetCabinetFragment.SetCabinetListener,SetDrawerFragment.SetDrawerFragmentListener,DrawerFragment1.deletDrawerFragmentListener,
        HardwareSetupFragment.hardwareSetupListener,EditTemplateFragment.editTemplateListen,SingleTemplateFragment.singleTemplateListen,SetupFragment.setuplisten,TemplateLineFragment.templateLineListen{
    private var scApp: SCApp? = null
    private var returnview = "login"
    private var dbManager: DBManager? = null
    private var mBackKeyPressed = false
    var handler =Handler()
    var download_handler=Handler()
    var mHandler: Handler = object : Handler() {
      override  fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> {
                    //在这里得到数据，并且可以直接更新UI
                    val data = msg.obj as String
                    Toast.makeText(this@AdminActivity,data,Toast.LENGTH_SHORT).show()
                    val editTemplateFragment = EditTemplateFragment()
                    replaceFragment(editTemplateFragment, R.id.fl_admin)
                }
                2 -> {
                    val data = msg.obj as String
                    Toast.makeText(this@AdminActivity,data,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(null != this.currentFocus) {
            val mInputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0)
        }
        return super.onTouchEvent(event)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        dbManager = DBManager(applicationContext)
        scApp = application as SCApp
        val userAccount =  scApp?.userInfo
        when(userAccount?.getUserPower()){
            SC_Const.ADMIN -> {
                returnview ="login"
                val adminfrag = AdminFragment()
                replaceFragment(adminfrag, R.id.fl_admin)
            }
            SC_Const.NORMAL -> {
                returnview ="login"
                val orinaryFragment = OrinaryFragment()
                replaceFragment(orinaryFragment, R.id.fl_admin)
            }
        }
        back_button.setOnClickListener({
            when(returnview){
                "login" -> {
                    if (!mBackKeyPressed) {
                        Toast.makeText(this, "再按一次退出登陆", Toast.LENGTH_SHORT).show()
                        mBackKeyPressed = true
                        Timer().schedule( object:TimerTask(){
                            override fun run() {
                                mBackKeyPressed = false
                            }
                        },2000)
                    }
                    else{
                        val intent =Intent()
                        intent.setClass(this,LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
                "admin" -> {
                    val adminfragment = AdminFragment()
                    replaceFragment(adminfragment, R.id.fl_admin)
                    returnview ="login"
                }
                "orinary" -> {
                    val orinaryFragment = OrinaryFragment()
                    replaceFragment(orinaryFragment, R.id.fl_admin)
                    returnview ="login"
                }
                "addPerson"-> {
                    val addPersonFragment = AddPersonFragment()
                    replaceFragment(addPersonFragment, R.id.fl_admin)
                    returnview ="admin"
                }
                "setup" -> {
                    val setupFragment = SetupFragment()
                    replaceFragment(setupFragment,R.id.fl_admin)
                    returnview = "admin"
                }
                "editTemplate" ->{
                    val editTemplateFragment = EditTemplateFragment()
                    replaceFragment(editTemplateFragment,R.id.fl_admin)
                    returnview = "admin"
                }
                "setCabinet" -> {
                    val setCabinetFragment = SetCabinetFragment()
                    replaceFragment(setCabinetFragment,R.id.fl_admin)
                    returnview = "setup"
                }
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode === KeyEvent.KEYCODE_BACK) {
            return true // return true 和 false 我都试过，都能屏蔽，原因还未知，希望知道的可以告诉我一下，谢谢
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun saveHardwareClick(text: String) {
        when(text) {
            "saveHardware" -> {
                val setupFragment = SetupFragment()
                replaceFragment(setupFragment,R.id.fl_admin)
                returnview = "admin"
            }
        }
    }


    override  fun deletDrawerButtonClick(text: String,drawerID:Int) {
        when(text) {
            "find_out" -> {
                returnview = "setCabinet"
                val setDrawer = SetDrawerFragment()
                val args = Bundle()
                args.putString("setDrawer",""+drawerID)
                args.putString("data","find_out")
                setDrawer.arguments=args
                replaceFragment(setDrawer, R.id.fl_admin)
            }
            "drawer_modify" -> {
                returnview = "setCabinet"
                val setDrawer = SetDrawerFragment()
                val args = Bundle()
                args.putString("setDrawer",""+drawerID)
                args.putString("data","drawer_modify")
                setDrawer.arguments=args
                replaceFragment(setDrawer, R.id.fl_admin)
            }
        }
    }
    override fun saveDrawerButtonClick(text: String) {
        if(text == "saveDrawer") {
            returnview = "setup"
            val setCabinet = SetCabinetFragment()
            replaceFragment(setCabinet, R.id.fl_admin)
        }
    }
    override fun savepersonButtonClick(text: String) {
        if(text == "save") {
            //returnview未定义
            val addPersonFragment = AddPersonFragment()
            replaceFragment(addPersonFragment, R.id.fl_admin)
        }
    }
    override fun addpersonButtonClick(text: String) {
        returnview = "addPerson"
        if (text == "addperson") {
            val editMessageFragment = EditPersonFragment()
            val args = Bundle()
            args.putString("editfile","addperson")
            editMessageFragment.arguments=args
            replaceFragment(editMessageFragment, R.id.fl_admin)
        }
    }
    override fun deletButtonClick(text: String) {
        when(text) {
            "delet" -> {
                returnview = "admin"
                val addPersonFragment = AddPersonFragment()
                replaceFragment(addPersonFragment, R.id.fl_admin)
            }
            "edit" -> {
                returnview = "addPerson"
                val editPersonFragment =  EditPersonFragment()
                val args = Bundle()
                args.putString("edit","editOther")
                editPersonFragment.arguments=args
                replaceFragment(editPersonFragment,R.id.fl_admin)

            }
        }
    }
    override fun orinaryButtonClick(text: String)
    {
        returnview = "orinary"
        when(text){
            "regentsearch" -> {
                val serachfrag = SerachFragment()
                replaceFragment(serachfrag, R.id.fl_admin)
            }
            "edit_flie" ->
            {
                val editMessageFragment = EditPersonFragment()
                val args = Bundle()
                args.putString("editfile","editperson")
                editMessageFragment.arguments=args
                replaceFragment(editMessageFragment, R.id.fl_admin)
            }
            "reagent_operation" ->{
                if(dbManager!!.sysSeting.size > 0){
                    returnview ="login"
                    val intent = Intent()
                    intent.setClass(this,OperationActivity::class.java)
                    startActivity(intent)
                }
                else
                    Toast.makeText(this,"请进行系统硬件设置",Toast.LENGTH_SHORT).show()
            }
            "record_query" ->{
                val recordFragment = RecordFragment()
                replaceFragment(recordFragment, R.id.fl_admin)
            }

            "orinary_template" ->{
                val editTemplateFragment = EditTemplateFragment()
                replaceFragment(editTemplateFragment, R.id.fl_admin)
            }
        }
    }

    override fun editTemplateButtonClick(text: String) {
        when(text) {
            "btn_single" -> {
                returnview = "editTemplate"
                val singleTemplateFragment = SingleTemplateFragment()
                replaceFragment(singleTemplateFragment, R.id.fl_admin)
            }
            "btn_clean" ->{
                val dialog = AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("是否要清空模板")
                        .setPositiveButton("确定", DialogInterface.OnClickListener{ dialogInterface, i ->
                            dbManager?.deleteAllReagentTemplate()
                            val editTemplateFragment = EditTemplateFragment()
                            replaceFragment(editTemplateFragment, R.id.fl_admin)
                            Toast.makeText(this,"试剂清空完成",Toast.LENGTH_SHORT).show()
                        })
                        .setNeutralButton("取消",null)
                        .create()
                dialog.show()
                dialog.window.setGravity(Gravity.CENTER)
            }
            "btn_import" ->{
                val edit= EditText(this)
                val dialog = AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setView(edit)
                        .setMessage("请输入试剂模板编号")
                        .setPositiveButton("确定", DialogInterface.OnClickListener{ dialogInterface, i ->
                            scApp?.templateID=edit.text.toString()
                            downLoad() //下载与导入模板的线程开启
                        })
                        .setNeutralButton("取消",null)
                        .create()
                dialog.show()
                dialog.window.setGravity(Gravity.CENTER)
            }
        }
    }
    override fun singleTemplateButtonClick(text: String,state: Int){
        when(text){
            "btn_save" ->{      //下拉框未添加，内容未进行判断
                returnview = "admin"
                val editTemplateFragment = EditTemplateFragment()
                replaceFragment(editTemplateFragment, R.id.fl_admin)
            }
        }

    }

    override fun templateLineButtonClick(text: String) {
        if(text=="delet"){

            returnview = "admin"
            val editTemplateFragment = EditTemplateFragment()
            replaceFragment(editTemplateFragment, R.id.fl_admin)
        }
    }

    override fun adminOnButtonClick(text: String) {
        returnview = "admin"
        when(text) {
            "reagent_search"-> {
                val serachfrag = SerachFragment()
                replaceFragment(serachfrag, R.id.fl_admin)
            }
            "personal_management"-> {
                val addPersonFragment = AddPersonFragment()
                replaceFragment(addPersonFragment, R.id.fl_admin)
            }
            " editflie" ->
            {
                val editMessageFragment = EditPersonFragment()
                val args = Bundle()
                args.putString("editfile","editperson")
                editMessageFragment.arguments=args
                replaceFragment(editMessageFragment, R.id.fl_admin)

            }
            "reagent_op"-> {

                if(dbManager!!.sysSeting.size > 0){
                    returnview ="login"
                    val intent = Intent()
                    intent.setClass(this,OperationActivity::class.java)
                    startActivity(intent)
                }

                else
                    Toast.makeText(this,"请进行系统硬件设置",Toast.LENGTH_SHORT).show()
            }

            "recordquery" -> {
                val recordFragment = RecordFragment()
                replaceFragment(recordFragment, R.id.fl_admin)
            }

            "reagent_template" -> {
                val editTemplateFragment = EditTemplateFragment()
                replaceFragment(editTemplateFragment, R.id.fl_admin)
            }

            "btn_setup" -> {
                val setupFragment = SetupFragment()
                replaceFragment(setupFragment, R.id.fl_admin)
            }
        }
    }

    override fun setupClick(text: String) {
        returnview = "setup"
        when (text){
            "binding" -> {
                val bingdingFragment = BingdingFragment()
                replaceFragment(bingdingFragment,R.id.fl_admin)
            }

            "setbox" -> {
                val setCabinet = SetCabinetFragment()
                replaceFragment(setCabinet, R.id.fl_admin)
            }

            "hardware" -> {
                val hardwareSetupFragment = HardwareSetupFragment()
                replaceFragment(hardwareSetupFragment, R.id.fl_admin)
            }
            "update"->{
                var manager= UpdateAppManager(this)
                manager.getUpdateMsg()
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
        returnview = "setCabinet"
        when (fragment){
            "setDrawer" -> {
                val setDrawer = SetDrawerFragment()
                val args = Bundle()
                args.putString("data","setDrawer")
                setDrawer.arguments = args
                replaceFragment(setDrawer, R.id.fl_admin)
            }
        }
    }



    fun templateToDB(filePath: String?): String {
        var ret = ""
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



    fun downLoad() {                    //下载线程
        object : Thread() {
            override fun run() {
                val path = "SmartCabinet/ReagentTemplate"
                val fileName = scApp?.templateID + ".csv"
                val urlStr = SC_Const.REAGENTTEMPLATEADDRESS + fileName
                val SDCard = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + ""
                val pathName = "$SDCard/$path/$fileName"//文件存储路径
                if (File(pathName).exists()) {
                    templateToDB(pathName)
                    val msg = Message()
                    msg.what = 0
                    msg.obj = "更新成功"
                    mHandler.sendMessage(msg)
                }
                else
                {
                    try {
                        /*
                         * 通过URL取得HttpURLConnection
                         * 要网络连接成功，需在AndroidMainfest.xml中进行权限配置
                         * <uses-permission android:name="android.permission.INTERNET" />
                         */
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

                        } else {

                            Toast.makeText(SCApp.getContext(), "试剂模板导入成功", Toast.LENGTH_SHORT).show()
                            val msg = Message()
                            msg.what = 0
                            msg.obj = "更新成功"
                            mHandler.sendMessage(msg)
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
        }
        }.start()  //开启一个线程
    }

}




