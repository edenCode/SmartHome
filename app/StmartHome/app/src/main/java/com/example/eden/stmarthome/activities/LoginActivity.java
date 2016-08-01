package com.example.eden.stmarthome.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eden.stmarthome.R;
import com.example.eden.stmarthome.utils.Constant;
import com.example.eden.stmarthome.utils.MyAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button loginBtn;
    Button registerBtn;
    Button resetBtn;
    CheckBox passCheck;
    EditText etName, etPass, etIp, etPort;
    TextView resetPass;
    LayoutInflater inflater;
    Intent intent;
    File file;
    boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_login);


        init();
        file = new File("/data/data/" + getPackageName() + "shareFile", "user.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String str = reader.readLine();
            JSONObject object = new JSONObject(str);
            if (object.getBoolean("bool")) {
                passCheck.setChecked(true);
                etName.setText(object.getString("user"));
                etPass.setText(object.getString("pass"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("Test", "onCreate");
    }


    private void init() {

        inflater = LayoutInflater.from(this);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        resetBtn = (Button) findViewById(R.id.resetIP);
        etName = (EditText) findViewById(R.id.etName);
        etPass = (EditText) findViewById(R.id.etPass);
        resetPass = (TextView) findViewById(R.id.resetPass);
        passCheck = (CheckBox) findViewById(R.id.checkid);

        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);
        resetPass.setOnClickListener(this);
        passCheck.setOnClickListener(this);


    }

    long lastClick = 0;

    @Override
    public void onClick(View v) {

        intent = new Intent(LoginActivity.this, MainActivity.class);
        if (System.currentTimeMillis() - lastClick <= 1000) {
            Toast.makeText(this, "按那么快干嘛啊", Toast.LENGTH_SHORT).show();
            lastClick = System.currentTimeMillis();
            return;
        }
        if (v.getId() == R.id.loginBtn) {
            Log.i("Test", "v.getId() == R.id.loginBtn");
            if (!etName.getText().toString().equals("") && !etPass.getText().toString().equals("")) {
                if ((etName.getText().toString().length() != 0) && (etName.getText().toString().length() != 0)) {

                    Log.i("Test", "onClick");
                    Constant.userName = etName.getText().toString();
                    MyAsyncTask task = new MyAsyncTask(this);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("userName", etName.getText().toString());
                        jsonObject.put("password", etPass.getText().toString());
                        Log.i("Test", "name = " + etName.getText().toString());
                        Log.i("Test", "password = " + etPass.getText().toString());
                        Log.i("Test", "length : " + ((short) jsonObject.toString().length()));
                        byte a[] = Constant.getByteData((byte) 0x01, (short) jsonObject.toString().length(), jsonObject.toString().getBytes());
                        for (int i = 0; i < a.length; i++) {
                            if (i < 1) {
                                Log.i("Test", " type : byte : " + a[i]);
                            } else if (i == 1) {
                                Log.i("Test", " type : byte : " + a[i]);
                            } else if (i > 1 && i < 3) {
                                Log.i("Test", "func : short :" + (short) (a[i] | a[i + 1] << 8));
                                Log.i("Test", "func : short :" + (short) (a[i] | a[i + 1] >> 8));
                            }

                        }
                        task.execute(Constant.getByteData((byte) 0x01, (short) jsonObject.length(), jsonObject.toString().getBytes()));
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                    Log.i("Test", "object length ---------> " + jsonObject.toString() + " len =" + jsonObject.length());

                }
            } else {
                Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (v.getId() == R.id.registerBtn) {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        }
        if (v.getId() == R.id.resetIP) {
            View view = inflater.inflate(R.layout.ipset_layout, null);
            etIp = (EditText) view.findViewById(R.id.ip);
            etPort = (EditText) view.findViewById(R.id.port);
            etIp.setText(Constant.url);
            etPort.setText(String.valueOf(Constant.port));
            new AlertDialog.Builder(this)
                    .setTitle("IP设置")
                    .setView(view)
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Constant.url = etIp.getText().toString();
                            Constant.port = Integer.parseInt(etPort.getText().toString());
                            Log.i("Test", "IP :" + Constant.url);
                            Log.i("Test", "Port :" + Constant.port);
                        }
                    }).create().show();
        }
        if (v.getId() == R.id.resetPass) {
            startActivity(new Intent(LoginActivity.this, ChangePassActivity.class));
        }

        if (v.getId() == R.id.checkid) {
            if (passCheck.isChecked()) {
                try {
                    isChecked = true;
                    FileWriter writer = new FileWriter(file);
                    JSONObject object = new JSONObject();
                    object.put("bool", isChecked);
                    object.put("user", etName.getText().toString());
                    object.put("pass", etPass.getText().toString());
                    writer.write(object.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                isChecked = false;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
