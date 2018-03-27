package com.example.smartcabinet;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import com.example.smartcabinet.util.CabinetInfo;
import com.example.smartcabinet.util.DBManager;
import com.example.smartcabinet.util.SerialPortInterface;
import com.example.smartcabinet.util.UserAccount;


/**
 * Created by Administrator on 2017/7/5 0005.
 */

public class SCApp extends Application {  //全局变量
    private DBManager dbManager;
    private UserAccount userInfo;
    private CabinetInfo cabinetInfo;
    private int Touchdrawer;
    private int Touchtable;
    private int TemplateNum;
    private String reagentID;
    private String serialPortID;
    SerialPortInterface spi;
    private static Context context;
    private String TemplateID;
    private int statue;
    @Override
    public void onCreate() {
        super.onCreate();
        dbManager = new DBManager(this);
        context = getApplicationContext();
        serialPortID = "/dev/ttyS4";
        //  init ZXing lib
        // ZXingLibrary.initDisplayOpinion(this);
    }
    public static Context getContext(){
        return context;
    }

    public DBManager getDbManager(){
        return dbManager;
    }

    public void setUserInfo(UserAccount u){
        userInfo = u;
    }

    public void setUserInfo(String id, String account, String password, int power ,String name,String phoneNum){
        userInfo = new UserAccount(id, account, password, power,name,phoneNum);
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
    public void setTemplateNum(int pos)
    {
        this.TemplateNum=pos;
    }
    public int getTemplateNum()
    {
        return TemplateNum;
    }

    public void setSpi(SerialPortInterface serialPort) {
        this.spi = serialPort;
    }
    public SerialPortInterface getSpi() {
        return spi;
    }
    public void setTemplateID(String id){
        this.TemplateID=id;
    }
    public String getTemplateID()
    {
        return TemplateID;
    }
    public void setReagentID(String id)
    {
        reagentID=id;
    }
    public String getReagentID()
    {
        return this.reagentID;
    }

    public void setUpdateTeamplate(int update){  statue = update; }
    public int getUpdateTeamplate(){
        return statue;
    }
}
