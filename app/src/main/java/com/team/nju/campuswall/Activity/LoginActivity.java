package com.team.nju.campuswall.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;


import com.team.nju.campuswall.Network.NetworkCallbackInterface;
import com.team.nju.campuswall.Network.StatusCode;
import com.team.nju.campuswall.Network.netRequest;
import com.team.nju.campuswall.R;
import com.team.nju.campuswall.Util.CommonUrl;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements NetworkCallbackInterface.NetRequestIterface {
    private ProgressDialog loginProgessDlg;
    private netRequest requestFragment;
    private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageView signup = (ImageView) findViewById(R.id.signup);
        ImageView login = (ImageView) findViewById(R.id.login);
        requestFragment=new netRequest(this,this);
        signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, signUpActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = ((EditText) findViewById(R.id.username)).getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();
                if (phone.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入手机号码", Toast.LENGTH_LONG).show();
                    ((EditText) findViewById(R.id.username)).setText("");
                    ((EditText) findViewById(R.id.password)).setText("");
                } else if (password.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
                    ((EditText) findViewById(R.id.username)).setText("");
                    ((EditText) findViewById(R.id.password)).setText("");
                } else {
                    Map map = new HashMap();
                    map.put("phone",phone);
                    map.put("password", password);
                    map.put("type", StatusCode.REQUEST_LOGIN);
                    requestFragment.httpRequest(map, CommonUrl.loginAccount);
                    loginProgessDlg= ProgressDialog.show(LoginActivity.this, "campusWall", "处理中", true, false);
                }
            }
        });
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if (requestUrl.equals(CommonUrl.loginAccount)) {//返回登录请求
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));

            if (code == StatusCode.REQUEST_LOGIN_SUCCESS) {
              //  Gson gson = new Gson();
              //  LoginDataModel loginModel = gson.fromJson(object.getJSONArray("contents").getJSONObject(0).toString(), LoginDataModel.class);
               // ACache cache = ACache.get(LoginActivity.this);
                loginProgessDlg.cancel();//进度条取消
                Intent intent = new Intent(getApplicationContext(), mainActivity.class);
                Bundle data = new Bundle();
                data.putString("phone", phone);
                intent.putExtras(data);
                startActivity(intent);
                finish();
                return;
            } else {
                loginProgessDlg.cancel();//进度条取消
               Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    @Override
    public void exception(IOException e, String requestUrl){

    }
}
