package com.team.nju.campuswall.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.team.nju.campuswall.R;

public class publishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        EditText title = (EditText) findViewById(R.id.publishTitle);
        Button emotion = (Button) findViewById(R.id.emotionTitleBtn);
        Button event = (Button) findViewById(R.id.eventTitleBtn);
        Button thing = (Button) findViewById(R.id.thingTitleBtn);
        ImageView add = (ImageView) findViewById(R.id.addIcon);
        Button publish = (Button) findViewById(R.id.publishBtn);
    }
}
