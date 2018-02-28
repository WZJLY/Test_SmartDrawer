package com.example.smartcabinet

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.EditText
import android.widget.Toast
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.SC_Const
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.fragment_line_person.*
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import kotlin.math.log

class AdminActivity : AppCompatActivity(),AdminFragment.AdminFragmentListener,OrinaryFragment.orinarybuttonlisten,PersonLineFragment.deletbuttonlisten,EditPersonFragment.addpersonbuttonlisten,EditMessageFragment.savepersonbuttonlisten ,SetCabinetFragment.SetCabinetListener,SetDrawerFragment.SetDrawerFragmentListener,DrawerFragment1.deletDrawerFragmentListener{
    private var scApp: SCApp? = null
    private var returnview = "login"
    private var dbManager: DBManager? = null
    var handler =Handler()
    var download_handler=Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == 3) {
                    download_handler = msg.obj as Handler
                    Toast.makeText(applicationContext, "子线程说："+msg.obj, Toast.LENGTH_LONG).show()
                    val backmsg = Message.obtain()
                    backmsg.what = 4
                    download_handler.sendMessage(backmsg)
                }


            }

        }
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
                    val editperson = EditPersonFragment()
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
            val editperson = EditPersonFragment()
            replaceFragment(editperson, R.id.framelayout)
        }
    }
    override fun addpersonButtonClick(text: String) {
        returnview = "editperson"
        if (text == "addperson")
        {

            val editMessageFragment = EditMessageFragment()
            val args = Bundle()
            args.putString("editfile","addperson")
            editMessageFragment.setArguments(args)
            replaceFragment(editMessageFragment, R.id.framelayout)

        }
    }
    override fun deletButtonClick(text: String) {
        returnview = "admin"
        if(text == "delet") {
           val editperson = EditPersonFragment()
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

                val editMessageFragment = EditMessageFragment()
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
                val editperson = EditPersonFragment()
                replaceFragment(editperson, R.id.framelayout)
            }
            " editflie" ->
            {
                val editMessageFragment =EditMessageFragment()
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
                builder.setView(edit)
                builder.setMessage("请输入试剂模板编号")
                builder.setPositiveButton("确定", DialogInterface.OnClickListener{ dialogInterface, i ->
                    val templateid = edit.text.toString()
                    
                })

                val thread = DownloadThread(handler)
                thread.start()
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
//  private  val handler = object : Handler() {
//        fun handieMessage(msg: Message) {
//            if(msg.what==1){
//                val path = "SmartCabinet/ReagentTemplate"
//                val fileName = "1519704145828" + ".csv"
//                val SDCard = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + ""
//                val pathName = "$SDCard/$path/$fileName"
//                templateToDB(pathName)
//                Log.d("data","else")
//
//            }
//        }
//    }
    @JavascriptInterface
    fun importAgentiaTemplate(id: String) {
        val path = "SmartCabinet/ReagentTemplate"
        val fileName = id + ".csv"
        val urlStr = SC_Const.REAGENTTEMPLATEADDRESS + fileName
        var output: OutputStream? = null
        val SDCard = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + ""
        val pathName = "$SDCard/$path/$fileName"//文件存储路径
        try {
            /*
                 * 通过URL取得HttpURLConnection
                 * 要网络连接成功，需在AndroidMainfest.xml中进行权限配置
                 * <uses-permission android:name="android.permission.INTERNET" />
                 */
            val url = URL(urlStr)
            val conn = url.openConnection() as HttpURLConnection
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

            val file = File(pathName)
            val input = conn.inputStream
            if (file.exists()) {
                Log.d("file already exists:", pathName)
            } else {
                Toast.makeText(this, "开始下载试剂模板", Toast.LENGTH_LONG).show()
                val dir = SDCard + "/" + path
                File(dir).mkdir()//新建文件夹
                file.createNewFile()//新建文件
                output = FileOutputStream(file)
                //读取大文件
                val buffer = ByteArray(4 * 1024)
                while (input.read(buffer) != -1) {
                    output!!.write(buffer)
                }
                output!!.flush()
            }

            if (templateToDB(pathName) == "")
                Toast.makeText(this, "试剂模板导入成功", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(this, "试剂模板导入失败", Toast.LENGTH_LONG).show()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            Toast.makeText(this, "该试剂模板编码不存在", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        } finally {
            try {
                output!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        return
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
                            if (lineArray[0] != null && lineArray[0] !== "") {
                                if (lineArray[5] == "") lineArray[5] = "1"
                                dbManager?.addReagentTemplate(lineArray[0], lineArray[1], lineArray[2], lineArray[3], lineArray[4], Integer.valueOf(lineArray[5]),
                                        lineArray[6], lineArray[7], lineArray[8], lineArray[9], lineArray[10], if (lineArray.size > 11) lineArray[11] else "")
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

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    fun getPath(context: Context, uri: Uri): String? {

        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)!!)

                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)
        return null
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                      selectionArgs: Array<String>?): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            if (cursor != null)
                cursor.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }



}




