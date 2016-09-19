package com.team.nju.campuswall.Activity;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.gson.Gson;
import com.team.nju.campuswall.Model.ACache;
import com.team.nju.campuswall.Model.LoginDataModel;
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
//import com.team.nju.campuswall.myView.photoChange;

public class profileActivity extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface{

    private netRequest requestFragment;
    ImageView photo;
    ImageView changeprofile;
    ImageView changephoto;
    TextView username;
    TextView school;
    TextView product;
    TextView join;
    TextView star;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        requestFragment= new netRequest(this,this);
        Map map = new HashMap();
        map.put("nickname","111" );
        map.put("type", StatusCode.REQUEST_PROFILE);
        requestFragment.httpRequest(map, CommonUrl.getProfile);

        TableLayout tableLayout =(TableLayout)findViewById(R.id.profileLayout);
//        final photoChange pc = new photoChange(profileActivity.this);
//        tableLayout.addView(pc);
        changeprofile=(ImageView)findViewById(R.id.changeProfile) ;
        changephoto =(ImageView)findViewById(R.id.changePhoto);
        photo = (ImageView)findViewById(R.id.photo);
        username=(TextView)findViewById(R.id.profile_username);
        school=(TextView)findViewById(R.id.profile_school);
        product=(TextView)findViewById(R.id.profile_product);
        join=(TextView)findViewById(R.id.profile_join);
        star=(TextView)findViewById(R.id.profile_star);

        changeprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        changephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if (requestUrl.equals(CommonUrl.loginAccount)) {//返回登录请求
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));

            if (code == StatusCode.REQUEST_PROFILE_SUCCESS) {
                Gson gson = new Gson();
               UserModel userModel = gson.fromJson(object.getJSONArray("contents").getJSONObject(0).toString(), UserModel.class);
               username.setText(userModel.getNickname());
                //......
                return;
            } else {
                Toast.makeText(profileActivity.this, "未知错误", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    @Override
    public void exception(IOException e, String requestUrl){
        Message msg=new Message();
        msg.what=StatusCode.REQUEST_FAILURE;
        msg.obj="网络请求失败";
        // mHandler.sendMessage(msg);
    }
}
