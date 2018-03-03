package com.example.smartcabinet.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**
 * Created by Administrator on 2017/6/10 0010.
 */

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;
    private String DBOPERATION = "DBManager";

    public DBManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public void tableUpgrade(){
        helper.createTable(db);
    }


    //----------------------------------user manage begin----------------------------//
    public void DeleteDBUser(String tableName){

    }
    public void deleteAccountByUserName(String userName){
        db.delete("user", "userName == ?", new String[]{ userName });
    }
    public void addAccount(UserAccount user) {
        if(!isAccountExist(user.userName)){
            db.execSQL("INSERT INTO user VALUES(null, ?, ?, ?, ?)", new Object[]{user.userId, user.userName, user.userPassword, user.userPower});
            Log.i(DBOPERATION, "Insert Success");
        }else{
            Log.i(DBOPERATION, "already exist!");
        }
    }

    public void deleteAccountByUserId(String userId){
        db.delete("user", "userId == ?", new String[]{ userId });
    }

    public void deleteAccount(UserAccount user) {
        db.delete("user", "userName == ?", new String[]{String.valueOf(user.userName)});
    }

    public void updateAccount(UserAccount user) {

    }
    public boolean isAccountExist(String strUserName) {
        Cursor cursor = db.query("user", new String[] {"userName"}, "userName=?", new String[] { strUserName }, null, null, null);
        if (cursor.moveToNext()) {
            return true;
        }
        return false;
    }

    public boolean isAccountExist(String strUserName, String strUserPWD) {
        Cursor cursor = db.query("user", new String[] {"userName", "userPassword", "userPower"}, "userName=? AND userPassword=?", new String[] { strUserName, strUserPWD }, null, null, null);
        if (cursor.moveToNext()) {
            Log.i(DBOPERATION, "find this user in DB - name:" + cursor.getString(cursor.getColumnIndex("userName")) + "\t password:" + cursor.getString((cursor.getColumnIndex("userPower"))));
            return true;
        }
        return false;
    }

    public UserAccount getUserAccountByUserId(String strUserId){
        Cursor cursor = db.query("user", new String[] {"userId", "userName", "userPassword", "userPower"}, "userId=?", new String[] { strUserId }, null, null, null);
        cursor.moveToNext();
        UserAccount userInfo = new UserAccount(
                cursor.getString(cursor.getColumnIndex("userId")),
                cursor.getString(cursor.getColumnIndex("userName")),
                cursor.getString(cursor.getColumnIndex("userPassword")),
                parseInt(cursor.getString(cursor.getColumnIndex("userPower")))
        );
        return userInfo;
    }

    public void updateAccountByUserId(String strUserId, String strUserName, String strUserPWD, int iUserPower){
        ContentValues cv = new ContentValues();
        cv.put("userId", strUserId);
        cv.put("userName", strUserName);
        cv.put("userPassword", strUserPWD);
        cv.put("userPower", iUserPower);
        db.update("user", cv, "userId=?", new String[] { strUserId });
    }

    public UserAccount getUserAccount(String strUserName, String strUserPWD){
        if(!isAccountExist(strUserName, strUserPWD)){
            Log.e(DBOPERATION, "user is not exist! error from getUserPower");
            return new UserAccount();
        }
        Cursor cursor = db.query("user", new String[] {"userId", "userName", "userPassword", "userPower"}, "userName=? AND userPassword=?", new String[] { strUserName, strUserPWD }, null, null, null);
        cursor.moveToNext();
        UserAccount userInfo = new UserAccount(
                cursor.getString(cursor.getColumnIndex("userId")),
                cursor.getString(cursor.getColumnIndex("userName")),
                cursor.getString(cursor.getColumnIndex("userPassword")),
                parseInt(cursor.getString(cursor.getColumnIndex("userPower")))
                );
        return userInfo;
    }

    public boolean queryUserByUserId(String strUserId){
        Cursor cursor = db.query("user", new String[] {"userName", "userPassword", "userPower"}, "userId=?", new String[] { strUserId }, null, null, null);
        if (cursor.moveToNext()) {
            Log.i(DBOPERATION, "find this user in DB - name:" + cursor.getString(cursor.getColumnIndex("userName")) + "\t password:" + cursor.getString((cursor.getColumnIndex("userPower"))));
            return true;
        }
        return false;
    }

    public ArrayList<UserAccount> getUsers(){
        Cursor cursor = db.rawQuery("select * from user", null);
        ArrayList<UserAccount> arrListUsers = new ArrayList<>();
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                UserAccount userAccount = new UserAccount(
                        cursor.getString(cursor.getColumnIndex("userId")),
                        cursor.getString(cursor.getColumnIndex("userName")),
                        cursor.getString(cursor.getColumnIndex("userPassword")),
                        parseInt(cursor.getString(cursor.getColumnIndex("userPower")))
                );
                arrListUsers.add(userAccount);
                cursor.moveToNext();
            }
        }
        return arrListUsers;
    }
    //----------------------------------user manage end----------------------------//

    //----------------------------------box manage begin----------------------------//
    public ArrayList<Box> getBoxes(){
        Cursor cursor = db.rawQuery("select * from box",null);
        ArrayList<Box> arrListBoxes = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                arrListBoxes.add(new Box(cursor.getInt(cursor.getColumnIndex("_id"))));
                cursor.moveToNext();
            }
        }
        return arrListBoxes;
    }
    public void addBox(int boxId){
        Cursor cursor = db.query("box", new String[] {"_id"}, "_id=?", new String[] { boxId + ""}, null, null, null);
        if(cursor.moveToNext()){
            Log.e("DB_ADD_BOX_ERROR", "The Id of Box Already Exist");
        }else{
            db.execSQL("INSERT INTO box VALUES(?)", new Object[]{boxId});
        }
    }
    public void deleteBox(int boxId){
        db.delete("box", "_id=?", new String[]{boxId + ""});
        deleteDrawer(boxId);
    }

    //----------------------------------box manage end----------------------------//



    //----------------------------------drawer manage begin----------------------------//
    public ArrayList<Drawer> getDrawers(){
        Cursor cursor = db.rawQuery("select * from drawer",null);
        ArrayList<Drawer> arrListDrawers = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                arrListDrawers.add(new Drawer(cursor.getInt(cursor.getColumnIndex("drawerId")), cursor.getInt(cursor.getColumnIndex("boxId")),  cursor.getInt(cursor.getColumnIndex("drawerSize"))));
                cursor.moveToNext();
            }
        }
        return arrListDrawers;
    }
    public void addDrawer(int drawerId, int boxId, int drawerSize){
        db.execSQL("INSERT INTO drawer VALUES(null, ?, ?, ?)", new Object[]{drawerId, boxId, drawerSize});
    }
    public void deleteDrawer(int drawerId, int boxId){
        db.delete("drawer", "drawerId=? AND boxId=?", new String[]{drawerId + "", boxId + ""});
    }
    public void deleteDrawer(int boxId){
        db.delete("drawer", "boxId=?", new String[]{boxId + ""});
    }
    public Drawer getDrawerByDrawerId(int strDrawerId){
        Cursor cursor = db.query("drawer", new String[] {"drawerId", "boxId", "drawerSize"}, "drawerId=?", new String[] {strDrawerId+""}, null, null, null);
        cursor.moveToNext();
        Drawer drawerInfo = new Drawer(
                parseInt(cursor.getString(cursor.getColumnIndex("drawerId"))),
               parseInt(cursor.getString(cursor.getColumnIndex("boxId"))),
               parseInt(cursor.getString(cursor.getColumnIndex("drawerSize")))
        );
        return drawerInfo;
    }


    public void udpateDrawerSize(int drawerId, int boxId, int drawerSize){
        ContentValues data=new ContentValues();
        data.put("drawerId",drawerId);
        data.put("boxId",boxId);
        data.put("drawerSize", drawerSize);
        db.update("drawer", data, "drawerId=? and boxId=?", new String[]{drawerId + "", boxId + ""});
    }
    //----------------------------------drawer manage end----------------------------//
/*
*
*
*  db.execSQL("CREATE TABLE IF NOT EXISTS reagent" +
                "(_id INTEGER PRIMARY KEY,
                 reagentId VARCHAR, reagentName VARCHAR, reagentAlias VARCHAR,
                 reagentFormalName VARCHAR, reagentChemName VARCHAR, reagentType INTEGER,
                 reagentPurity VARCHAR, reagentSize VARCHAR, reagentTotalSize VARCHAR,
                 reagentCreater VARCHAR, reagentGoodsID VARCHAR, reagentUnit INTEGER,
                 reagentDensity VARCHAR, reagentInvalidDate VARCHAR" cabinetId VARCHAR,
                 drawerId VARCHAR, reagentSize VARCHAR, reagentPosition VARCHAR,
                  status INTEGER, reagentUser VARCHAR)");
* */
    public ArrayList<Reagent> getReagents(){
        Cursor cursor = db.rawQuery("select * from reagent order by reagentName",null);
        ArrayList<Reagent> arrListReagents = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Reagent reagent = new Reagent(cursor.getString(cursor.getColumnIndex("reagentId")), cursor.getString(cursor.getColumnIndex("reagentName")), cursor.getString(cursor.getColumnIndex("reagentAlias")),
                        cursor.getString(cursor.getColumnIndex("reagentFormalName")), cursor.getString(cursor.getColumnIndex("reagentChemName")), cursor.getInt(cursor.getColumnIndex("reagentType")),
                        cursor.getString(cursor.getColumnIndex("reagentPurity")), cursor.getString(cursor.getColumnIndex("reagentSize")), cursor.getString(cursor.getColumnIndex("reagentTotalSize")),
                        cursor.getString(cursor.getColumnIndex("reagentCreater")), cursor.getString(cursor.getColumnIndex("reagentGoodsID")), cursor.getInt(cursor.getColumnIndex("reagentUnit")),
                        cursor.getString(cursor.getColumnIndex("reagentDensity")), cursor.getString(cursor.getColumnIndex("reagentInvalidDate")), cursor.getString(cursor.getColumnIndex("cabinetId")),
                        cursor.getString(cursor.getColumnIndex("drawerId")), cursor.getString(cursor.getColumnIndex("reagentPosition")),
                        cursor.getInt(cursor.getColumnIndex("status")), cursor.getString(cursor.getColumnIndex("reagentUser")));
                arrListReagents.add(reagent);
                cursor.moveToNext();
            }
        }
        return arrListReagents;
    }

    public boolean isReagentExist(String strReagentId) {
        Cursor cursor = db.query("reagent", null, "reagentId=? ", new String[] { strReagentId}, null, null, null);
        if (cursor.moveToNext()) {
            return true;
        }
        return false;
    }           //wzj  add

    public Reagent getReagentById(String strReagentId) {
        Cursor cursor = db.rawQuery("select * from reagent where reagentId = '" + strReagentId + "'",null);
        Reagent reagent = null;
        ArrayList<Reagent> arrListReagents = new ArrayList<>();
        if (cursor.moveToFirst()) {
            if (!cursor.isAfterLast()) {
                reagent = new Reagent(cursor.getString(cursor.getColumnIndex("reagentId")), cursor.getString(cursor.getColumnIndex("reagentName")), cursor.getString(cursor.getColumnIndex("reagentAlias")),
                        cursor.getString(cursor.getColumnIndex("reagentFormalName")), cursor.getString(cursor.getColumnIndex("reagentChemName")), cursor.getInt(cursor.getColumnIndex("reagentType")),
                        cursor.getString(cursor.getColumnIndex("reagentPurity")), cursor.getString(cursor.getColumnIndex("reagentSize")), cursor.getString(cursor.getColumnIndex("reagentTotalSize")),
                        cursor.getString(cursor.getColumnIndex("reagentCreater")), cursor.getString(cursor.getColumnIndex("reagentGoodsID")), cursor.getInt(cursor.getColumnIndex("reagentUnit")),
                        cursor.getString(cursor.getColumnIndex("reagentDensity")), cursor.getString(cursor.getColumnIndex("reagentInvalidDate")), cursor.getString(cursor.getColumnIndex("cabinetId")),
                        cursor.getString(cursor.getColumnIndex("drawerId")), cursor.getString(cursor.getColumnIndex("reagentPosition")),
                        cursor.getInt(cursor.getColumnIndex("status")), cursor.getString(cursor.getColumnIndex("reagentUser")));
                return reagent;
            }
        }
        return reagent;
    }

    public Reagent getReagentByPos(String strDrawerId,String strReagentPos) {
        Cursor cursor =  db.query("reagent", null, "drawerId=? and reagentPosition=?", new String[] { strDrawerId,strReagentPos }, null, null, null);
        Reagent reagent = null;
        if (cursor.moveToFirst()) {
            if (!cursor.isAfterLast()) {
                reagent = new Reagent(cursor.getString(cursor.getColumnIndex("reagentId")), cursor.getString(cursor.getColumnIndex("reagentName")), cursor.getString(cursor.getColumnIndex("reagentAlias")),
                        cursor.getString(cursor.getColumnIndex("reagentFormalName")), cursor.getString(cursor.getColumnIndex("reagentChemName")), cursor.getInt(cursor.getColumnIndex("reagentType")),
                        cursor.getString(cursor.getColumnIndex("reagentPurity")), cursor.getString(cursor.getColumnIndex("reagentSize")), cursor.getString(cursor.getColumnIndex("reagentTotalSize")),
                        cursor.getString(cursor.getColumnIndex("reagentCreater")), cursor.getString(cursor.getColumnIndex("reagentGoodsID")), cursor.getInt(cursor.getColumnIndex("reagentUnit")),
                        cursor.getString(cursor.getColumnIndex("reagentDensity")), cursor.getString(cursor.getColumnIndex("reagentInvalidDate")), cursor.getString(cursor.getColumnIndex("cabinetId")),
                        cursor.getString(cursor.getColumnIndex("drawerId")), cursor.getString(cursor.getColumnIndex("reagentPosition")),
                        cursor.getInt(cursor.getColumnIndex("status")), cursor.getString(cursor.getColumnIndex("reagentUser")));
                return reagent;
            }
        }
        return reagent;
    }                                   //wzj  add


    public void deleteReagentById(String strReagentId){
        db.delete("reagent", "reagentId == ?", new String[]{ strReagentId });
    }
    public void deleteReagentByPos(String strDrawerId,String strReagentPos)
    {
        db.delete("reagent", "drawerId == ? and reagentPosition ==?", new String[]{ strDrawerId,strReagentPos });
    }

    public void addReagent(String strReagentId, String strReagentName, String strReagentAlias,
                           String strReagentFormalName, String strReagentChemName, int iReagentType,
                           String strReagentPurity, String strReagentSize, String strReagentTotalSize,
                           String strReagentCreater, String strReagentGoodsID, int iReagentUnit,
                           String strReagentDensity, String strReagentInvalidDate, String strCabinetId,
                           String strDrawerId, String strReagentPos, int iStatus, String strReagentUser) {
        Reagent reagent = new Reagent(strReagentId, strReagentName, strReagentAlias,
                strReagentFormalName, strReagentChemName, iReagentType,
                strReagentPurity, strReagentSize, strReagentTotalSize,
                strReagentCreater, strReagentGoodsID, iReagentUnit,
                strReagentDensity, strReagentInvalidDate, strCabinetId,
                strDrawerId, strReagentPos, iStatus, strReagentUser);
        db.execSQL("INSERT INTO reagent VALUES(null, ?, ?, ?, " +
                "?, ?, ?, " +
                "?, ?, ?, " +
                "?, ?, ?, " +
                "?, ?, ?, " +
                "?, ?, ?, ?)", new Object[]{strReagentId, strReagentName, strReagentAlias,
                strReagentFormalName, strReagentChemName, iReagentType,
                strReagentPurity, strReagentSize, strReagentTotalSize,
                strReagentCreater, strReagentGoodsID, iReagentUnit,
                strReagentDensity, strReagentInvalidDate, strCabinetId,
                strDrawerId, strReagentPos, iStatus, strReagentUser});
    }

    public void backReagent(String strReagentId, String strSize){
        ContentValues data=new ContentValues();
        data.put("status",SC_Const.EXIST);
        data.put("reagentSize", strSize);
        data.put("reagentUser", "");
        db.update("reagent", data, "reagentId=?", new String[]{strReagentId + ""});
    }

    public void updateReagentStatus(String strReagentId, int iStatus, String strUserId){
        Log.e(strReagentId, iStatus + "");
        ContentValues data=new ContentValues();
        data.put("status",iStatus);
        data.put("reagentUser",strUserId);
        db.update("reagent", data, "reagentId=?", new String[]{strReagentId + ""});
    }
    public void updateReagentSize(String strReagentId, String strReagentSize){
        ContentValues data=new ContentValues();
        data.put("reagentSize",strReagentSize);
        db.update("reagent", data, "reagentId=?", new String[]{strReagentId + ""});
    }
    public void updateReagentStatusByPos(String strDrawerId,String strReagentPos,String strReagentUser, int iStatus){
        ContentValues data=new ContentValues();
        data.put("status",iStatus);
        data.put("reagentUser",strReagentUser);
        db.update("reagent", data, "drawerId == ? and reagentPosition ==?", new String[]{strDrawerId,strReagentPos});
    }

    public void addReagentTemplate(String strReagentId, String strReagentName, String strReagentAlias,
                                   String strReagentFormalName, String strReagentChemName, int iReagentType,
                                   String strReagentPurity, String strReagentSize,
                                   String strReagentCreater, String strReagentGoodsID, String strReagentUnit,
                                   String strReagentDensity) {
        ReagentTemplate reagentTemplate = new ReagentTemplate(strReagentId, strReagentName, strReagentAlias,
                strReagentFormalName, strReagentChemName, iReagentType,
                strReagentPurity, strReagentSize,
                strReagentCreater, strReagentGoodsID, strReagentUnit,
                strReagentDensity);
        db.execSQL("INSERT INTO reagentTemplate VALUES(null, ?, ?, ?, " +
                "?, ?, ?, " +
                "?, ?, ?, " +
                "?, ?, ?)", new Object[]{strReagentId, strReagentName, strReagentAlias,
                strReagentFormalName, strReagentChemName, iReagentType,
                strReagentPurity, strReagentSize, strReagentCreater,
                strReagentGoodsID, strReagentUnit, strReagentDensity});
    }

    public ArrayList<ReagentTemplate> getReagentTemplate(){
        Cursor cursor = db.rawQuery("select * from reagentTemplate order by reagentName",null);
        ArrayList<ReagentTemplate> arrListReagentTempate = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ReagentTemplate reagentTemplate = new ReagentTemplate(cursor.getString(cursor.getColumnIndex("reagentId")), cursor.getString(cursor.getColumnIndex("reagentName")), cursor.getString(cursor.getColumnIndex("reagentAlias")),
                        cursor.getString(cursor.getColumnIndex("reagentFormalName")), cursor.getString(cursor.getColumnIndex("reagentChemName")), cursor.getInt(cursor.getColumnIndex("reagentType")),
                        cursor.getString(cursor.getColumnIndex("reagentPurity")), cursor.getString(cursor.getColumnIndex("reagentSize")), cursor.getString(cursor.getColumnIndex("reagentCreater")),
                        cursor.getString(cursor.getColumnIndex("reagentGoodsID")), cursor.getString(cursor.getColumnIndex("reagentUnit")), cursor.getString(cursor.getColumnIndex("reagentDensity")));
                arrListReagentTempate.add(reagentTemplate);
                cursor.moveToNext();
            }
        }
        return arrListReagentTempate;
    }

    //获取试剂柜编号
    public String getCabinetNo() {
        Cursor cursor = db.rawQuery("select * from cabinet_no",null);
        if (cursor.moveToFirst()) {
            if (!cursor.isAfterLast()) {
                return cursor.getString(cursor.getColumnIndex("cabinet_no"));
            }
        }
        return "";
    }

    public void addCabinetNo(String cabinentNo) {
        db.execSQL("INSERT INTO cabinet_no VALUES(?)", new Object[]{cabinentNo});
    }

    public void deleteAllReagentTemplate()
    {
        db.execSQL("DELETE FROM reagentTemplate");
    }

    public void closeDB() {
        db.close();
    }
}
