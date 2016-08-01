package com.example.eden.stmarthome.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Switch;
import com.example.eden.stmarthome.R;
import com.example.eden.stmarthome.utils.Constant;
import com.example.eden.stmarthome.utils.MyAsyncTask;
import org.json.JSONException;
import org.json.JSONObject;

public class DeviceActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView ivLed1, ivLed2, ivFen, ivDoor;
    Switch ivLed1on, ivLed2on, ivFenOn, ivDoorOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_device);
        ivDoor = (ImageView) findViewById(R.id.door);
        ivLed1 = (ImageView) findViewById(R.id.led1);
        ivLed2 = (ImageView) findViewById(R.id.led2);
        ivLed1on = (Switch) findViewById(R.id.ledon);
        ivLed2on = (Switch) findViewById(R.id.ledon2);
        ivFen = (ImageView) findViewById(R.id.feng);
        ivFenOn = (Switch) findViewById(R.id.openfen);
        ivDoorOn = (Switch) findViewById(R.id.opendoor);

        ivLed1on.setOnClickListener(this);
        ivLed2on.setOnClickListener(this);
        ivFenOn.setOnClickListener(this);
        ivDoorOn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        MyAsyncTask task = new MyAsyncTask(this);
        JSONObject object = new JSONObject();
        try {
            switch (v.getId()) {
                case R.id.ledon:
                    if (ivLed1on.isChecked()) {
                        object.put("userName", Constant.userName);
                        object.put("userToken", Constant.userToken);
                        object.put("deviceCode", 0);
                        object.put("deviceNumber", 0);
                        task.execute(Constant.getByteData((byte) 0x08, new Integer(object.length()).shortValue(), object.toString().getBytes()));
                        ivLed1.setImageResource(R.drawable.openled_ico);
                        ivLed1on.setText("ON");
                    } else {
                        object.put("userName", Constant.userName);
                        object.put("userToken", Constant.userToken);
                        object.put("deviceCode", 1);
                        object.put("deviceNumber", 0);
                        task.execute(Constant.getByteData((byte) 0x08, new Integer(object.length()).shortValue(), object.toString().getBytes()));
                        ivLed1.setImageResource(R.drawable.closeled_ico);
                        ivLed1on.setText("Off");
                    }
                    break;
                case R.id.ledon2:
                    if (ivLed2on.isChecked()) {
                        object.put("userName", Constant.userName);
                        object.put("userToken", Constant.userToken);
                        object.put("deviceCode", 0);
                        object.put("deviceNumber", 1);
                        task.execute(Constant.getByteData((byte) 0x08, new Integer(object.length()).shortValue(), object.toString().getBytes()));
                        ivLed2.setImageResource(R.drawable.openled_ico);
                    } else {
                        object.put("userName", Constant.userName);
                        object.put("userToken", Constant.userToken);
                        object.put("deviceCode", 1);
                        object.put("deviceNumber", 1);
                        task.execute(Constant.getByteData((byte) 0x08, new Integer(object.length()).shortValue(), object.toString().getBytes()));
                        ivLed2.setImageResource(R.drawable.closeled_ico);
                    }
                    break;
                case R.id.opendoor:
                    if (ivFenOn.isChecked()) {
                        object.put("userName", Constant.userName);
                        object.put("userToken", Constant.userToken);
                        object.put("deviceCode", 0);
                        object.put("deviceNumber", 0);
                        task.execute(Constant.getByteData((byte) 0x0a, new Integer(object.length()).shortValue(), object.toString().getBytes()));
                        ivDoor.setImageResource(R.drawable.opendoor_ico);
                    } else {
                        object.put("userName", Constant.userName);
                        object.put("userToken", Constant.userToken);
                        object.put("deviceCode", 1);
                        object.put("deviceNumber", 0);
                        task.execute(Constant.getByteData((byte) 0x0a, new Integer(object.length()).shortValue(), object.toString().getBytes()));
                        ivDoor.setImageResource(R.drawable.closedoor_ico);
                    }
                    break;
                case R.id.openfen:
                    if (ivDoorOn.isChecked()) {
                        object.put("userName", Constant.userName);
                        object.put("userToken", Constant.userToken);
                        object.put("deviceCode", 0);
                        object.put("deviceNumber", 0);
                        task.execute(Constant.getByteData((byte) 0x09, new Integer(object.length()).shortValue(), object.toString().getBytes()));
                       ivFen.setImageResource(R.drawable.openfan_ico);
                    } else {
                        object.put("userName", Constant.userName);
                        object.put("userToken", Constant.userToken);
                        object.put("deviceCode", 1);
                        object.put("deviceNumber", 0);
                        task.execute(Constant.getByteData((byte) 0x09, new Integer(object.length()).shortValue(), object.toString().getBytes()));
                        ivFen.setImageResource(R.drawable.closefan_ico);
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
