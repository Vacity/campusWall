package com.team.nju.campuswall.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
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

import java.util.ArrayList;
import java.util.List;

import com.team.nju.campuswall.BL.personBLImpl;
import com.team.nju.campuswall.BLService.personBLService;
import com.team.nju.campuswall.R;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    personBLService ps = new personBLImpl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView login=(ImageView)findViewById(R.id.login);
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                String username=((EditText)findViewById(R.id.username)).getText().toString();
                String password=((EditText)findViewById(R.id.password)).getText().toString();
                if(username==null) {
                    Toast.makeText(LoginActivity.this, "请输入用户名或手机", Toast.LENGTH_LONG).show();
                    ((EditText)findViewById(R.id.username)).setText("");
                    ((EditText)findViewById(R.id.password)).setText("");
                }else if(password==null){
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
                    ((EditText)findViewById(R.id.username)).setText("");
                    ((EditText)findViewById(R.id.password)).setText("");
                }else{
                    if(ps.login(username,password)) {
                        // 正确登录
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "输入的密码不正确", Toast.LENGTH_LONG).show();
                        ((EditText)findViewById(R.id.username)).setText("");
                        ((EditText)findViewById(R.id.password)).setText("");
                    }
                }
            }
        });
    }
}

