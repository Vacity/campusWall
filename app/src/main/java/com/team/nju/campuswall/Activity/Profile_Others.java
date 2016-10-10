package com.team.nju.campuswall.Activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.team.nju.campuswall.Model.UserModel;
import com.team.nju.campuswall.Network.NetworkCallbackInterface;
import com.team.nju.campuswall.Network.StatusCode;
import com.team.nju.campuswall.Network.netRequest;
import com.team.nju.campuswall.R;
import com.team.nju.campuswall.Util.CommonUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Profile_Others extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface {

    private ImageView gender;
    private Button exit;
    private TextView username;
    ImageView photo;
    TextView signature;
    UserModel userModel;
    String nickname;
    boolean sex;
    String sign;
    String userurl;
    private String authorphone;
    private int authorid;
    private netRequest requestFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_others);
        gender = (ImageView)findViewById(R.id.picture_gender);
        exit = (Button)findViewById(R.id.bt_exit);
        username = (TextView) findViewById(R.id.profile_username);
        signature = (TextView) findViewById(R.id.profile_signature);
        photo = (ImageView) findViewById(R.id.photo);
        requestFragment=new netRequest(this,this);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle data = getIntent().getExtras();
        authorid=data.getInt("authorid");
        initInfo();
    }

    private void initInfo() {
        Map map = new HashMap();
        map.put("type", StatusCode.REQUEST_AUTHOR_PHONE);
        map.put("uid", authorid);
        requestFragment.httpRequest(map, CommonUrl.getProfile);
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        Message message = new Message();
        if (requestUrl.equals(CommonUrl.getProfile)) {
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));

            if (code == StatusCode.REQUEST_AUTHOR_PHONE_SUCCESS) {
                authorphone=object.getString("contents");
                message.what=StatusCode.REQUEST_AUTHOR_PHONE_SUCCESS;
                handler.sendMessage(message);
            }
            if (code == StatusCode.REQUEST_PROFILE_SUCCESS) {
                Gson gson = new Gson();
                userModel = gson.fromJson(object.getJSONArray("contents").getJSONObject(0).toString(), UserModel.class);
                message.what = StatusCode.REQUEST_PROFILE_SUCCESS;
                handler.sendMessage(message);
                return;
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StatusCode.REQUEST_AUTHOR_PHONE_SUCCESS:
                    //获得到了作者的手机，请求信息
                    Map map = new HashMap();
                    map.put("phone",authorphone);
                    map.put("type", StatusCode.REQUEST_PROFILE);
                    requestFragment.httpRequest(map, CommonUrl.getProfile);
                    break;
                 case StatusCode.REQUEST_PROFILE_SUCCESS://成功返回信息
                {
                    nickname = userModel.getUalais();
                    sign = userModel.getUsign();
                    sex = userModel.getUsex();
                    userurl=userModel.getUserurl();
                    if(!sex)
                        gender.setImageResource(R.drawable.woman);
                    else
                        gender.setImageResource(R.drawable.man);

                    if(userurl!=null) {
                        Glide.with(getApplicationContext())
                                .load(userurl).centerCrop()
                                .into(photo);
                    }
                    username.setText(nickname);

                    if (!signature.equals(""))
                        signature.setText(sign);
                    else
                        signature.setText("未填写");
                    break;
                }
            }
        }
    };

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
