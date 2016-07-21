package com.team.nju.campuswall.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.team.nju.campuswall.R;
//import com.team.nju.campuswall.myView.photoChange;

public class profileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TableLayout tableLayout =(TableLayout)findViewById(R.id.profileLayout);
//        final photoChange pc = new photoChange(profileActivity.this);
//        tableLayout.addView(pc);
        ImageView photo = (ImageView)findViewById(R.id.photo);
        TextView username=(TextView)findViewById(R.id.profile_username);
        TextView school=(TextView)findViewById(R.id.profile_school);
        TextView product=(TextView)findViewById(R.id.profile_product);
        TextView join=(TextView)findViewById(R.id.profile_join);
        TextView star=(TextView)findViewById(R.id.profile_star);
    }
}
