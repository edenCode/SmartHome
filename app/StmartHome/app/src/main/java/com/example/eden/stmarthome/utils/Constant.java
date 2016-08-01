package com.example.eden.stmarthome.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Eden on 2016/7/19.
 */
public class Constant {
    public static String url = "192.168.1.119";
    public static int port = 8888;
    public static String socketErr = "登录失败";
    public static String status = null;
    public static int deviceCode = 0;
    public static  int type = 0;
    public static  int  function = 0;
    public static  int  length = 0;
    public static  String jsonStr = null;
    public  static  String userToken = null;
    public static  String userName = null;

    public Constant(int type,int function,int length,String s) {
        Constant.type = type;
        Constant.function = function;
        Constant.length = length;
        Constant.jsonStr = s;
    }

    public static int toInt(byte[] bRefArr) {
        int iOutcome = 0;
        byte bLoop;
        for ( int i =0; i<4 ; i++) {
            bLoop = bRefArr[i];
            iOutcome = iOutcome | (bLoop & 0xFF) << (8 * i);
        }
        return iOutcome;
    }

    public static byte[] getByteData(byte func, short len, byte[] jsonStr) {

        ByteArrayOutputStream baos =new ByteArrayOutputStream();
        DataOutputStream dos = null;

        try {

            dos = new DataOutputStream(baos);
            dos.writeByte(0xaa);//-86
            dos.writeByte((int)func);//1
            dos.writeShort((int)len);
            dos.write(jsonStr);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
}
