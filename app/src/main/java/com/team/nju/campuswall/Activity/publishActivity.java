package com.team.nju.campuswall.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.team.nju.campuswall.Network.NetworkCallbackInterface;
import com.team.nju.campuswall.Network.StatusCode;
import com.team.nju.campuswall.Network.netRequest;
import com.team.nju.campuswall.R;
import com.team.nju.campuswall.Util.CommonUrl;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class publishActivity extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface{

    private netRequest requestFragment;
    private ProgressDialog publishProgressDlg;
    private boolean emotionClicked = false;
    private boolean eventClicked = false;
    private boolean thingClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        requestFragment = new netRequest(this, this);

        final Button emotion = (Button) findViewById(R.id.emotionTitleBtn);
        final Button event = (Button) findViewById(R.id.eventTitleBtn);
        final Button thing = (Button) findViewById(R.id.thingTitleBtn);
        final ImageView add = (ImageView) findViewById(R.id.addIcon);
        final Button publish = (Button) findViewById(R.id.publishBtn);



        emotion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                emotionClicked = !emotionClicked;
            }
        });

        event.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                eventClicked = !eventClicked;
            }
        });

        thing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thingClicked = !thingClicked;
            }
        });

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap();
                String titleText = ((EditText) findViewById(R.id.publishTitle)).getText().toString();//1.标题
                ArrayList<String> tags = new ArrayList<String>();//2.类别
                if (eventClicked) {
                    tags.add(emotion.getText().toString());
                }

                if (eventClicked) {
                    tags.add(event.getText().toString());
                }

                if (thingClicked) {
                    tags.add(thing.getText().toString());
                }

                if ((!titleText.equals("")) && (tags.size() > 0)) {
                    //正确发出动态
                    map.put("title", titleText);
                    map.put("contentTypes", tags);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
                    String date = df.format(new Date());//4.日期
                    String contentText = ((EditText) findViewById(R.id.publishContent)).getText().toString();//5.内容
                    map.put("date", date);
                    map.put("content", contentText);
//                    map.put("type", StatusCode.)//请求码
                } else {
                    Toast.makeText(publishActivity.this, "请完善动态内容", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
//        if (requestUrl.equals(CommonUrl.))
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
