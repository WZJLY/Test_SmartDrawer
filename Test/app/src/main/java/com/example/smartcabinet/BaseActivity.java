package com.example.smartcabinet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Alex on 2017/11/11.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private long mAdvertisingTime =300 * 1000;//定时跳转广告时间
    public CountDownTimer mCountDownTimer;
    public Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //屏幕常亮，如果不需要，可以注释
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //全屏效果
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mContext = this;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //有按下动作时取消定时
                if (mCountDownTimer != null) {
                    mCountDownTimer.cancel();
                }
                break;
            case MotionEvent.ACTION_UP:
                //抬起时启动定时
                startAD();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁时停止定时
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //当activity不在前台是停止定时
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAD();
    }

    public void startAD() {
        if (mCountDownTimer == null) {
            mCountDownTimer = new CountDownTimer(mAdvertisingTime, 1000l) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    //TODO:定时完成后的操作
                    Intent advertising  = new Intent(BaseActivity.this,AdvertisementActivity.class);
                    startActivity(advertising);
                    finish();
                }
            };
            mCountDownTimer.start();
        } else {
            mCountDownTimer.start();
        }
    }


}
