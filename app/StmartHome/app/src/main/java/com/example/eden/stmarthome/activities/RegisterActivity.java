package com.example.eden.stmarthome.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eden.stmarthome.R;
import com.example.eden.stmarthome.utils.Constant;
import com.example.eden.stmarthome.utils.MyAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    Button registerBtn;
    EditText etNameR, etPassR, etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_register);

        this.registerBtn = (Button) findViewById(R.id.registerBtn);
        this.etNameR = (EditText) findViewById(R.id.etNameR);
        this.etPassR = (EditText) findViewById(R.id.etPassR);
        this.etPhone = (EditText) findViewById(R.id.phone);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!etNameR.getText().toString().equals("") && !etPassR.getText().toString().equals("")) {
            if ((etNameR.getText().toString() != null) && (etNameR.getText().toString() != null)) {

                Log.i("Test", "onClick");
                JSONObject object = new JSONObject();
                MyAsyncTask task = new MyAsyncTask(this);
                try {
                    object.put("phoneNumber", etPhone.getText().toString());
                    object.put("userName", etNameR.getText().toString());
                    object.put("password", etPassR.getText().toString());
                    task.execute(Constant.getByteData((byte) 0x00, new Integer(object.length()).shortValue(), object.toString().getBytes()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
