
package com.example.eden.stmarthome.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.eden.stmarthome.R;
import com.example.eden.stmarthome.utils.Constant;
import com.example.eden.stmarthome.utils.MyAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePassActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etPhone,etUserName;
    Button sendBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_password);
        etPhone =(EditText) findViewById(R.id.oldpass);
        etUserName =(EditText) findViewById(R.id.newPass);
        sendBtn = (Button) findViewById(R.id.pregisterBtn);

        sendBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        MyAsyncTask task = new MyAsyncTask(this);
        JSONObject object = new JSONObject();
        if(!(etPhone.getText().toString().equals("")) && !(etUserName.getText().toString().equals(""))){
            if(etPhone.getText().length() != 0 && etUserName.getText().toString().length() != 0){
                try {
                    object.put("oldPassword",etUserName.getText().toString());
                    object.put("newPassword",etPhone.getText().toString());
                    task.execute(Constant.getByteData((byte) 0x02,new Integer(object.length()).shortValue(),object.toString().getBytes()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
