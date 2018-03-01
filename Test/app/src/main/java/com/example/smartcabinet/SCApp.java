package com.example.smartcabinet;

import android.app.Application;
import android.util.DisplayMetrics;

import com.example.smartcabinet.util.CabinetInfo;
import com.example.smartcabinet.util.DBManager;
import com.example.smartcabinet.util.UserAccount;


/**
 * Created by Administrator on 2017/7/5 0005.
 */

public class SCApp extends Application {
    private DBManager dbManager;
    private UserAccount userInfo;
    private CabinetInfo cabinetInfo;
    private int Touchdrawer;
    private int Touchtable;
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

    public CabinetInfo getCabinetInfo() {
        return cabinetInfo;
    }

    public void setCabinetInfo(CabinetInfo cabinetInfo) {
        this.cabinetInfo = cabinetInfo;
    }

    public void setTouchdrawer(int pos)
    {
        this.Touchdrawer=pos;
    }
    public int getTouchdrawer()
    {
        return Touchdrawer;
    }
    public void setTouchtable(int pos)
    {
        this.Touchtable=pos;
    }
    public int getTouchtable()
    {
        return Touchtable;
    }

}
