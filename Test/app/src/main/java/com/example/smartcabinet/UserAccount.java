package com.example.smartcabinet;

/**
 * Created by Administrator on 2017/6/10 0010.
 */

public class UserAccount {
    public int _id;
    public String userId;
    public String userName;
    public String userPassword;
    public int userPower;   //用户权限，对应SC_Const.java中的常量

    public UserAccount() {
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

    public int getUserPower(){
        return this.userPower;
    }

    public UserAccount(String ID, String account, String password, int power) {
        this.userId = ID;
        this.userName = account;
        this.userPassword = password;
        this.userPower = power;
    }
}
