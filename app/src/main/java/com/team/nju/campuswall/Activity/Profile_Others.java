package com.team.nju.campuswall.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Profile_Others extends AppCompatActivity {

    private ImageView gender;
    private Button exit;
    private TextView username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_others);
        gender = (ImageView)findViewById(R.id.picture_gender);
        exit = (Button)findViewById(R.id.bt_exit);
        username = (TextView)findViewById(R.id.username);
    }

    private void setGenderPicture(int genderNum){
        switch (genderNum){
            case 1:
                gender.setImageDrawable(R.drawable.man);break;
            case 0:
                gender.setImageDrawable(R.drawable.woman);break;
        }
    }
}
