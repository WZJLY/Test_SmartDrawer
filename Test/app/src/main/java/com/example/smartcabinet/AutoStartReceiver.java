package com.example.smartcabinet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 实现开机启动
 * @author Owner
 */
public class AutoStartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent start = new Intent(context, LoginActivity.class);
        start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(start);
    }
}