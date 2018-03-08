package com.example.smartcabinet

import android.app.Application
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.preference.PreferenceActivity
import android.util.Log
import android.widget.Toast
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

        Looper.loop()   //Looper好像一定要放在最后才有效。
    }
}
