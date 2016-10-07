package com.team.nju.campuswall.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.team.nju.campuswall.Network.NetworkCallbackInterface;
import com.team.nju.campuswall.Network.StatusCode;
import com.team.nju.campuswall.Network.netRequest;
import com.team.nju.campuswall.R;
import com.team.nju.campuswall.Util.CommonUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class publishActivity extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface{

    private netRequest requestFragment;
    private ProgressDialog publishProgressDlg;
//    private boolean emotionClicked = false;
//    private boolean eventClicked = false;
//    private boolean thingClicked = false;
    private int type=1;
    private String phone;  //author
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        Bundle data = getIntent().getExtras();
        phone= data.getString("phone");
        requestFragment = new netRequest(this, this);

        final ImageButton emotion = (ImageButton) findViewById(R.id.emotionTitleBtn);
        final ImageButton event = (ImageButton) findViewById(R.id.eventTitleBtn);
        final ImageButton thing = (ImageButton) findViewById(R.id.thingTitleBtn);
        final ImageView add = (ImageView) findViewById(R.id.addIcon);
        final Button publish = (Button) findViewById(R.id.publishBtn);



        emotion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            if(type==2)
                return;
                else{
                type=2;
                emotion.setImageResource(R.drawable.emotion_two);
                event.setImageResource(R.drawable.event_one);
                thing.setImageResource(R.drawable.thing_one);
            }
            }
        });

        event.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(type==1)
                    return;
                else{
                    type=1;
                    emotion.setImageResource(R.drawable.emotion_one);
                    event.setImageResource(R.drawable.event_two);
                    thing.setImageResource(R.drawable.thing_one);
                }
            }
        });

        thing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==3)
                    return;
                else{
                    type=3;
                    emotion.setImageResource(R.drawable.emotion_one);
                    event.setImageResource(R.drawable.event_one);
                    thing.setImageResource(R.drawable.thing_two);
                }
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
                String contentText = ((EditText) findViewById(R.id.publishContent)).getText().toString();//5.内容
                if ((!titleText.equals("")) && (!contentText.equals("")) ){
                    //正确发出动态
                    map.put("author",phone);
                    map.put("title", titleText);
                    map.put("category",type);   //1，2，3分别对应
                    map.put("content", contentText);
                    map.put("type", StatusCode.REQUEST_CREATE_MESSAGE);  //请求码

                    requestFragment.httpRequest(map, CommonUrl.publish);
                } else {
                    Toast.makeText(publishActivity.this, "请完善动态内容", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if (requestUrl.equals(CommonUrl.publish)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));

            if (code == StatusCode.REQUEST_CREATE_SUCCESS) {
               // progessDlg.cancel();
                Intent intent = new Intent(getApplicationContext(), mainActivity.class);
                Bundle data = new Bundle();
                data.putString("phone", phone);
                intent.putExtras(data);
                startActivity(intent);
                finish();
                return;
            } else {
                //  progessDlg.cancel();//进度条取消
                Toast.makeText(publishActivity.this, "发表失败", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
