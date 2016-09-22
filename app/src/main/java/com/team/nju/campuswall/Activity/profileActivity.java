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
    TextView signature;

    String phone;
    String nickname;
    String password;
    String sex;
    String sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle data = getIntent().getExtras();
        phone= data.getString("phone");

        requestFragment= new netRequest(this,this);
        Map map = new HashMap();
        map.put("phone",phone );
        map.put("type", StatusCode.REQUEST_PROFILE);
        requestFragment.httpRequest(map,CommonUrl.getProfile);
      //  requestFragment.httpRequest(map, CommonUrl.getProfile);

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
        signature=(TextView)findViewById(R.id.profile_signature);
        username.setText(nickname);
        if(!signature.equals(""))
            signature.setText(sign);
        else
            signature.setText("未填写");
        changeprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profileActivity.this,EditProfile.class);
                Bundle data = new Bundle();
                data.putString("phone", phone);
                data.putString("nickname",nickname);
                data.putString("sex",sex);
                data.putString("password",password);
                data.putString("signature",sign);
                //...
                intent.putExtras(data);
                startActivity(intent);
                finish();
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
        if (requestUrl.equals(CommonUrl.getProfile)) {
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));

            if (code == StatusCode.REQUEST_PROFILE_SUCCESS) {
                Gson gson = new Gson();
               UserModel userModel = gson.fromJson(object.getJSONArray("contents").getJSONObject(0).toString(), UserModel.class);

               nickname=userModel.getUalais();
                password=userModel.getUpassword();
                sign=userModel.getUsign();
                sex=userModel.getUsex();

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
//        Message msg=new Message();
//        msg.what=StatusCode.REQUEST_FAILURE;
//        msg.obj="网络请求失败";
//        // mHandler.sendMessage(msg);
    }
}
