package com.example.eden.stmarthome.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.eden.stmarthome.activities.LoginActivity;
import com.example.eden.stmarthome.activities.MainActivity;
import com.example.eden.stmarthome.activities.SetPassActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/**
 * Created by Eden on 2016/7/21.
 */
public class MyAsyncTask extends AsyncTask<byte[], Void, Constant> {

    Context context;
    DataOutputStream dos;

    public MyAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Constant doInBackground(byte[]... params) {
        OutputStream os = null;
        InputStream is = null;
        Socket  socket = null;
        Constant constant = null;
        StringBuffer sb = new StringBuffer();
        byte[] data = new byte[1024];
        try {
            Log.i("Test", "-------> params[0] :" + new String(params[0]).toString());
            Log.i("Test", "ip : " + Constant.url);
            Log.i("Test", "port : " + Constant.port);
            socket = new Socket(Constant.url,Constant.port);
            boolean isConnection = socket.isConnected() && !socket.isClosed();
            Log.i("Test","sokcet :  start ");
            if (isConnection ) {
                os = socket.getOutputStream();
                dos = new DataOutputStream(os);
                dos.write(params[0]);
                os.flush();

                is = socket.getInputStream();
                Log.i("Test", " length : " + is.toString().length());
                DataInputStream dis  = new DataInputStream(is);
                int type =  dis.readUnsignedByte();
                Log.i("Test"," type : " + Integer.toHexString(type));
                Log.i("Test"," type : " + type);
                int func = dis.readUnsignedByte();
                Log.i("Test"," func : " + Integer.toHexString(func));
                Log.i("Test","func : " + func);
                short len =(short) dis.readUnsignedShort();
                Log.i("Test"," len : " + Integer.toHexString(len));
                Log.i("Test","len : " + len);
                byte[] str = new byte[len];
                dis.read(str,0,str.length);
                Log.i("Test","str : " + new String(str).toString());
                constant = new Constant(type,func,len,new String(str).toString());

                //             byte[] str = new byte[is.toString().length()];
 //               dis.read(str);
               // Log.i("Test","jsonstr : " + new String(str).toString());
                //constant = getDataFromSocket(new String(str).toString());
                //  constant = new Constant(type,func,len,new String(str,"utf-8").toString());

            }else {

                Log.i("Test","没有连接 ");
            }



        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(socket != null) {
                    is.close();
                    os.close();
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return constant;
    }

    @Override
    protected void onPostExecute(Constant constant) {
        super.onPostExecute(constant);
        if(Constant.type == 0xff){
            Log.i("Test","成功啦");
           doLogical(Constant.function,Constant.jsonStr);
        }
    }
    public Constant getDataFromSocket(String jsonstr){
            Constant constant = null;
        try {
            InputStream is = new ByteArrayInputStream(jsonstr.getBytes("utf-8"));
            DataInputStream dis = new DataInputStream(is);
            byte type = (byte) dis.readUnsignedByte();
            Log.i("Test"," type : " + Integer.toHexString(type));
            byte func = (byte) dis.readUnsignedByte();
            Log.i("Test"," func : " + Integer.toHexString(func));
                 //   Log.i("Test","func : " + func);
            short len = (short) dis.readUnsignedShort();
            Log.i("Test"," len : " + Integer.toHexString(len));
           // Log.i("Test","len : " +len);
            byte[] str = new byte[len];
            dis.read(str);
            Log.i("Test","str : " + new String(str).toString());
            constant = new Constant(type,func,len,new String(str).toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  constant;
    }

    public void doLogical(int func,String str) {
        try {
            JSONObject object = new JSONObject(str);
            switch (func) {
                case 0x00:// register
                     int state = object.getInt("stateCode");
                    if(state == 0){
                        Toast.makeText(context,"注册成功",Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(context,LoginActivity.class);
                          context.startActivity(intent);
                    }else {
                        Toast.makeText(context,"注册失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 0x01://登录
                    Constant.userToken = object.getString("userToken");

                    if(object.getInt("stateCode") == 0 && Constant.userToken != null){
                        Toast.makeText(context,"登录成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                    }else {
                        Toast.makeText(context,"登录失败，查无此人",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 0x02://忘记密码
                    if(object.getInt("stateCode") == 0 ){
                        Toast.makeText(context,"发送成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, SetPassActivity.class);
                        context.startActivity(intent);
                    }else if (object.getInt("stateCode") == 2){
                        Toast.makeText(context,"用户名错误",Toast.LENGTH_SHORT).show();
                    }else if(object.getInt("stateCode") == 4){
                        Toast.makeText(context,"手机号错误",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context,"未知错误",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 0x03://修改密码
                    if(object.getInt("stateCode") == 0){
                        Toast.makeText(context,"修改成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context,LoginActivity.class);
                        context.startActivity(intent);
                    }else if(object.getInt("stateCode") == 0){
                        Toast.makeText(context,"修改不成功",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 0x04://请求温度
                    if(object.getInt("stateCode") == 0){

                    }
                    break;
                case 0x05://请求湿度
                    break;
                case 0x06://请求光照
                    break;
                case 0x07://请求三轴
                    break;
                case 0x08://关灯
                    break;
                case 0x09://关闭风扇
                    break;
                case 0x0a://关闭门
                    break;
                case 0x0b://获得视频文件
                    break;
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

    }
}
