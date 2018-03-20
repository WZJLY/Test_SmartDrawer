package com.example.smartcabinet.util;

public class CabinetInfo {
    public String CabinetNo;
public String CabinetServiceCode;
public String SerialNumber;
    public String getCabinetNo() {
        return CabinetNo;
    }
public String getCabinetServiceCode(){return CabinetServiceCode;}
    public  CabinetInfo(String cabinetNo,String cabinetServiceCode,String serialNumber) {
        this.CabinetNo = cabinetNo;
        this.CabinetServiceCode=cabinetServiceCode;
        this.SerialNumber=serialNumber;
    }
    public String getSerialNumber() {
        return SerialNumber;
    }
}
