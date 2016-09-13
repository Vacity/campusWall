package com.team.nju.campuswall.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.team.nju.campuswall.R;

public class signUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        TextView login =(TextView) findViewById(R.id.link_login);
        Button signup=(Button) findViewById(R.id.btn_signup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signUpActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinner=(Spinner)findViewById(R.id.input_school);
                spinner.getSelectedItem();
                String phone=((EditText)findViewById(R.id.input_phone)).getText().toString();
                String username=((EditText)findViewById(R.id.input_account)).getText().toString();
                String password=((EditText)findViewById(R.id.input_password)).getText().toString();

                if(phone=="") {
                    Toast.makeText(signUpActivity.this, "请输入手机号码", Toast.LENGTH_LONG).show();
                    ((EditText)findViewById(R.id.input_phone)).setText("");
                    ((EditText)findViewById(R.id.input_account)).setText("");
                    ((EditText)findViewById(R.id.input_password)).setText("");
                }else if(username=="") {
                    Toast.makeText(signUpActivity.this, "请输入昵称", Toast.LENGTH_LONG).show();
                    ((EditText)findViewById(R.id.input_phone)).setText("");
                    ((EditText)findViewById(R.id.input_account)).setText("");
                    ((EditText)findViewById(R.id.input_password)).setText("");
                }else if(password==""){
                    Toast.makeText(signUpActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
                    ((EditText)findViewById(R.id.input_phone)).setText("");
                    ((EditText)findViewById(R.id.input_account)).setText("");
                    ((EditText)findViewById(R.id.input_password)).setText("");
                }else{
//                   todo
                    if(true) {
                        // 正确注册

                    }
                }
            }
        });
    }


}
