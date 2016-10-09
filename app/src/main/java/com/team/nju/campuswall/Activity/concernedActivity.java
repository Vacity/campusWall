package com.team.nju.campuswall.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.team.nju.campuswall.R;

import java.util.List;


public class concernedActivity extends AppCompatActivity {

    TextView title;
    ListView list;
    Button exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concerned);

        title=(TextView)findViewById(R.id.text_title);
        list=(ListView)findViewById(R.id.concernedlist);
        exit = (Button)findViewById(R.id.bt_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
