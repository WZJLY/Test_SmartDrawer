package com.example.smartcabinet;

import android.app.Application;


/**
 * Created by Administrator on 2017/7/5 0005.
 */

public class SCApp extends Application {
    private DBManager dbManager;
    private UserAccount userInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        dbManager = new DBManager(this);

        //  init ZXing lib
        // ZXingLibrary.initDisplayOpinion(this);
    }

    public DBManager getDbManager(){
        return dbManager;
    }

    public void setUserInfo(UserAccount u){
        userInfo = u;
    }

    public void setUserInfo(String id, String account, String password, int power){
        userInfo = new UserAccount(id, account, password, power);
    }

    public UserAccount getUserInfo(){
        return userInfo;
    }

    public void clearUserInfo(){ userInfo = null; }




}
