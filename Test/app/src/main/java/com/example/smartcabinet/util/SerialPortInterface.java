package com.example.smartcabinet.util;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android_serialport_api.SerialPort;

//new serial port
public class SerialPortInterface extends AppCompatActivity {
    EditText mReceive;
    FileOutputStream mOutputStream = null;
    FileInputStream mInputStream = null;
    SerialPort sp;
    Context context;

    public SerialPortInterface(Context context) {
        this.context = context;
        try {
            sp = new SerialPort(new File("/dev/ttyS3"), 38400, 0);
            mOutputStream = (FileOutputStream) sp.getOutputStream();
            mInputStream = (FileInputStream) sp.getInputStream();
        } catch (SecurityException e) {
            Toast.makeText(this.context,"锁控板连接异常", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(this.context,"锁控板连接异常", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /*
    * 输入字符串w 返回lrc校验部分
    * @param  w=需要传输的信息  ID是地址
    * @return finaldata
    */
    public String Lrc(String w) {
        char[] m = w.toCharArray();
        int x = 0;
        int length = m.length;
        int[] lrcdata = new int[length];
        for (int i = 0; i < length; i++) {
            if (m[i] >= 'A')
                lrcdata[i] = m[i] - 'A' + 10;
            else
                lrcdata[i] = m[i] - '0';
        }
        for (int i = 0; i < length / 2; i++) {
            x += (lrcdata[2 * i] * 16 + lrcdata[2 * i + 1]);
        }
        x = x % 256;
        x = 256 - x;
        String finaldata = Integer.toHexString(x % 256).toUpperCase();
        return finaldata;
    }

    /**
     * 发送数据
     *
     * @param sendData
     * @return
     */
    public int UART_Send(String sendData) {
        if(mOutputStream == null){
            Toast.makeText(this.context,"无法发送串口指令", Toast.LENGTH_SHORT).show();
            return -1;
        }

        byte[] mBuffer = (sendData + "\r\n").getBytes();
        try {
            mOutputStream.write(mBuffer);
        } catch (IOException e) {
            Toast.makeText(this.context,"发送串口指令失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return -1;
        }
        return 1;
    }


    /**
     * 发送开锁指令
     *
     * @param DID：锁控板地址号
     * @param drawerNu：抽屉号
     */
    public int sendOpenLock(int DID, int drawerNu) {
        StringBuilder drawerData = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            if (drawerNu == i + 1)
                drawerData.append("1");
            else
                drawerData.append("0");
        }
        String str = String.format("%02x", DID).toUpperCase() + "21" + drawerData;
        String sendData = ":" + str + Lrc(str);
        Log.d("data", sendData);
        UART_Send(sendData);
        return 1;
    }

    /**
     * 发送查询锁状态
     *
     * @param DID ：锁控板地址号, 柜子（板子）编号
     */
    public String sendGetStat(int DID) {
        String str = String.format("%02x", DID).toUpperCase() + "11";
        String sendData = ":" + str + Lrc(str);
        Log.d("data", sendData);
        UART_Send(sendData);
        String result;
        result = readData();
        return result;
    }

    /**
     * 控制LED等
     *
     * @param DID：锁控板地址号
     * @param data：1     开启；0关闭
     */
    public int sendLED(int DID, int data) {
        String str = String.format("%02x", DID) + "41" + String.format("%02x", data);
        String sendData = ":" + str + Lrc(str);
        Log.d("data", sendData);
        UART_Send(sendData);
        return 1;
    }

    /**
     * 控制风扇
     *
     * @param DID：锁控板地址号
     * @param data：1     开启；0关闭
     */
    public int sendFan(int DID, int data) {
        String str = String.format("%02x", DID).toUpperCase() + "31" + String.format("%02x", data);
        String sendData = ":" + str + Lrc(str);
        Log.d("data", sendData);
        UART_Send(sendData);
        return 1;
    }

    /**
     * 接收数据
     * @param
     * @return
     */
    public String readData() {
        String readDatas = null;
        String Error = "";
        String lrc;
        byte[] buffer2 ;
        byte[] buffer3 = new byte[2];

        if(mInputStream ==  null){
            Toast.makeText(this.context,"无法读取串口数据", Toast.LENGTH_SHORT).show();
            return Error;
        }

        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(50);
                if (mInputStream.available() > 0) {
                    if (mInputStream != null) {
                        byte[] buffer = new byte[2048];
                        int size = mInputStream.read(buffer);
                        if (size > 21) {
                            int head = 0;
                            while (head < size) {
                                if (buffer[head] != ':') {
                                    head++;
                                    continue;
                                }
                                if (size - head - 1 > 20) {
                                    buffer2 = new byte[20];
                                    for (int j = 0; j < 20; j++) {
                                        buffer2[j] = buffer[head + j + 1];
                                    }
                                    buffer3[0] = buffer[head + 21];
                                    buffer3[1] = buffer[head + 22];
                                    readDatas = new String(buffer2);
                                    lrc = new String(buffer3);
                                    if (lrc.equals(Lrc(readDatas))) {
                                        return readDatas;
                                    } else {
                                        return Error;
                                    }
                                } else
                                    return Error;
                            }
                            break;
                        } else
                            break;
                    }
                    if (i == 9) {
                        break;
                    }
                }
            }catch(Exception e){
                return Error;
            }
        }
        return Error;
    }
}