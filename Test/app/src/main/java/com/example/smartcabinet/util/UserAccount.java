package com.example.smartcabinet.util;

/**
 * Created by Administrator on 2017/6/10 0010.
 */

public class UserAccount {
    public int id;
    public String userId;
    public String userName;
    public String userPassword;
    public int userPower;   //用户权限，对应SC_Const.java中的常量
    public String phoneNumber;
    public String userAccount;
public UserAccount(){

}
    public String getUserId(){
        return this.userId;
    }

    public String getUserName(){
        return this.userName;
    }

    public String getUserPassword(){
        return this.userPassword;
    }

    public int   getUserPower(){
        return this.userPower;
    }

    public String getUserAccount(){return  this.userAccount;}

    public String getPhoneNumber(){return  this.phoneNumber;}

    public UserAccount(String ID, String name, String password, int power,String account,String number) {
        this.userId = ID;
        this.userName = name;
        this.userPassword = password;
        this.userPower = power;
        this.userAccount=account;
        this.phoneNumber=number;
    }
}
