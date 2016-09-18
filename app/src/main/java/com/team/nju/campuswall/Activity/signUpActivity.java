package com.team.nju.campuswall.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.team.nju.campuswall.Model.ACache;
import com.team.nju.campuswall.Model.LoginDataModel;
import com.team.nju.campuswall.Network.NetworkCallbackInterface;
import com.team.nju.campuswall.Network.StatusCode;
import com.team.nju.campuswall.Network.netRequest;
import com.team.nju.campuswall.R;
import com.team.nju.campuswall.Util.CommonUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class signUpActivity extends Activity implements NetworkCallbackInterface.NetRequestIterface{
    private netRequest requestFragment;
    private ProgressDialog signupProgessDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        requestFragment=new netRequest(this,this);
        TextView login = (TextView) findViewById(R.id.link_login);
        Button signup = (Button) findViewById(R.id.btn_signup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap();
                Spinner spinner = (Spinner) findViewById(R.id.input_school);
                spinner.getSelectedItem();
                String phone = ((EditText) findViewById(R.id.input_phone)).getText().toString();
                String username = ((EditText) findViewById(R.id.input_account)).getText().toString();
                String password = ((EditText) findViewById(R.id.input_password)).getText().toString();

                    if (!phone.equals("") && !username.equals("") && !password.equals("")) {
                        // 正确注册
                        map.put("phone", phone);
                        map.put("nickname", username);
                        map.put("password", password);
                        map.put("school","nju");
                        map.put("type", StatusCode.REQUEST_REGISTER);
                        requestFragment.httpRequest(map, CommonUrl.registerAccount);
                        signupProgessDlg = ProgressDialog.show(signUpActivity.this, "campusWall", "处理中", true, false);
                        return;
                    } else {
                        Toast.makeText(signUpActivity.this, "请完善注册信息", Toast.LENGTH_LONG).show();
                    }
              //  }
            }
        });

    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if (requestUrl.equals(CommonUrl.registerAccount)) {
            try {
                JSONObject object = new JSONObject(result);
                int code = Integer.valueOf(object.getString("code"));

                if (code== StatusCode.RECIEVE_REGISTER_SUCCESS) {
                    signupProgessDlg.cancel();//进度条取消
                    Intent intent = new Intent(getApplicationContext(), showMessagesActivity.class);
                    intent.putExtra("result", "注册成功");
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}






























