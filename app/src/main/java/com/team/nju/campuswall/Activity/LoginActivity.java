package com.team.nju.campuswall.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;


import com.team.nju.campuswall.Model.ACache;
import com.team.nju.campuswall.Model.LoginDataModel;
import com.team.nju.campuswall.Network.NetworkCallbackInterface;
import com.team.nju.campuswall.Network.StatusCode;
import com.team.nju.campuswall.Network.netRequest;
import com.team.nju.campuswall.R;
import com.team.nju.campuswall.Util.CommonUrl;

import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements NetworkCallbackInterface.NetRequestIterface {
    private ProgressDialog loginProgessDlg;
    private netRequest requestFragment;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageView signup = (ImageView) findViewById(R.id.signup);
        ImageView login = (ImageView) findViewById(R.id.login);

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
                String username = ((EditText) findViewById(R.id.username)).getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();
                if (username.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入用户名或手机", Toast.LENGTH_LONG).show();
                    ((EditText) findViewById(R.id.username)).setText("");
                    ((EditText) findViewById(R.id.password)).setText("");
                } else if (password.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
                    ((EditText) findViewById(R.id.username)).setText("");
                    ((EditText) findViewById(R.id.password)).setText("");
                } else {
                    Map map = new HashMap();
                    map.put("phone", username);
                    map.put("password", password);
                    map.put("type", StatusCode.REQUEST_LOGIN);
                    requestFragment.httpRequest(map, CommonUrl.loginAccount);
                    loginProgessDlg= ProgressDialog.show(LoginActivity.this, "shacus", "处理中", true, false);
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
                Gson gson = new Gson();
                LoginDataModel loginModel = gson.fromJson(object.getJSONArray("contents").getJSONObject(0).toString(), LoginDataModel.class);
                ACache cache = ACache.get(LoginActivity.this);
                loginProgessDlg.cancel();//进度条取消
                Intent intent = new Intent(getApplicationContext(), showMessagesActivity.class);
                intent.putExtra("result", "登录成功");
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
                return;
            } else {
                loginProgessDlg.cancel();//进度条取消
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
//                Message msg=mHandler.obtainMessage();
//                msg.what= StatusCode.REQUEST_FAILURE;
//                msg.obj=object.getString("contents");
//                mHandler.sendMessage(msg);
                return;
            }
        }
    }

    @Override
    public void exception(IOException e, String requestUrl){
        Message msg=new Message();
        msg.what=StatusCode.REQUEST_FAILURE;
        msg.obj="网络请求失败";
       // mHandler.sendMessage(msg);
    }
}
