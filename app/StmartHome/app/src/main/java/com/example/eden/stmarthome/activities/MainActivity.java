package com.example.eden.stmarthome.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eden.stmarthome.R;
import com.example.eden.stmarthome.utils.Constant;
import com.example.eden.stmarthome.utils.MyAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvTemp, tvHum, tvSun;
    TextView tvTime, tvYear, tvDay;
    ImageView ivVideo, ivShow, ivDev, ivPlay;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    JSONObject object = (JSONObject) msg.obj;
                    switch (msg.arg1) {
                        case 0x04:
                            tvTemp.setText(object.getInt("temperature") + "");
                            break;
                        case 0x05:
                            tvHum.setText(object.getInt("humidity") + "");
                            break;
                        case 0x06:
                            tvHum.setText(object.getInt("light") + "");
                            break;
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();
        // 循环 获得温度
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = null;
                    Socket socket = new Socket(Constant.url,Constant.port);
                    while(true){
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
                        JSONObject object = new JSONObject(new String(str).toString());
                        Message msg  = new Message();
                        msg.obj = object;
                        msg.arg1 = func;
                        msg.what = 0;
                        mHandler.sendMessage(msg);
                        Log.i("Test","str : " + new String(str).toString());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void init() {
        tvTemp = (TextView) findViewById(R.id.temp);
        tvHum = (TextView) findViewById(R.id.humidity);
        tvSun = (TextView) findViewById(R.id.aqi);
        tvTime = (TextView) findViewById(R.id.time);
        tvYear = (TextView) findViewById(R.id.year);
        tvDay = (TextView) findViewById(R.id.week);
        ivVideo = (ImageView) findViewById(R.id.video);
        ivShow = (ImageView) findViewById(R.id.music);
        ivDev = (ImageView) findViewById(R.id.dev);
        ivPlay = (ImageView) findViewById(R.id.play);

        tvTemp.setOnClickListener(this);
        tvHum.setOnClickListener(this);
        tvSun.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        tvYear.setOnClickListener(this);
        tvDay.setOnClickListener(this);
        ivVideo.setOnClickListener(this);
        ivShow.setOnClickListener(this);
        ivDev.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        MyAsyncTask task = new MyAsyncTask(this);
        JSONObject object = new JSONObject();
        try {


            switch (v.getId()) {
                case R.id.temp:
                    object.put("userName", Constant.userName);
                    object.put("userToken", Constant.userToken);
                    object.put("deviceNumber", 0);
                    task.execute(Constant.getByteData((byte) 0x04, new Integer(object.length()).shortValue(), object.toString().getBytes()));
                    break;
                case R.id.humidity:
                    object.put("userName", Constant.userName);
                    object.put("userToken", Constant.userToken);
                    object.put("deviceNumber", 0);
                    task.execute(Constant.getByteData((byte) 0x05, new Integer(object.length()).shortValue(), object.toString().getBytes()));
                    break;
                case R.id.aqi:
                    object.put("userName", Constant.userName);
                    object.put("userToken", Constant.userToken);
                    object.put("deviceNumber", 0);
                    task.execute(Constant.getByteData((byte) 0x06, new Integer(object.length()).shortValue(), object.toString().getBytes()));
                    break;
                case R.id.time:
                    break;
                case R.id.year:
                    break;
                case R.id.week:
                    break;
                case R.id.video:
                  //  startActivity(new I);
                    break;
                case R.id.music:
                    break;
                case R.id.dev:
                    startActivity(new Intent(MainActivity.this,DeviceActivity.class));
                    break;
                case R.id.play:
                    startActivity(new Intent(MainActivity.this,SimpleActivity.class));
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
