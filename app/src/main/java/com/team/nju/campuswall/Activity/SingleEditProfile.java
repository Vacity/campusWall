package com.team.nju.campuswall.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.team.nju.campuswall.R;

public class SingleEditProfile extends AppCompatActivity {

    private Button save;
    private EditText et;
    private TextView title;
    private String info;
    private String src;
    private String phone;
    private String oldnickname;
    private String oldpassword;
    private String oldsignature;
    private String oldsex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_edit_profile);

        Bundle data = getIntent().getExtras();
        src = (String)data.getString("src");
        phone = data.getString("phone");
        oldnickname=data.getString("nickname");
        oldpassword=data.getString("password");
        oldsignature=data.getString("signature");
        oldsex=data.getString("sex");
        save = (Button)findViewById(R.id.bt_save);
        et = (EditText) findViewById(R.id.tv_normal);
        title = (TextView) findViewById(R.id.text_title) ;
        switch (src){
            case "oldnickname":
                et.setText(oldnickname);
                title.setText("修改昵称");
                break;
            case "oldpassword":
                et.setText(oldpassword);
                title.setText("修改密码");
                break;
            case "oldsignature":
                et.setText(oldsignature);
                title.setText("修改个性签名");
                break;
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info = et.getText().toString();
                Intent intent = new Intent(SingleEditProfile.this, EditProfile.class);
                Bundle data = new Bundle();
                switch (src){
                    case "oldnickname":
                        oldnickname = info;
                        break;
                    case "oldpassword":
                        oldpassword = info;
                        break;
                    case "oldsignature":
                        oldsignature = info;
                        break;
                }
                data.putString("oldnickname",oldnickname);
                data.putString("oldpassword",oldpassword);
                data.putString("oldsex",oldsex);
                data.putString("oldsignature",oldsignature);
                data.putString("phone",phone);
                intent.putExtras(data);
                startActivity(intent);
            }
        });
    }
}
