package com.team.nju.campuswall.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class EditProfile extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface{
    private String[] genderArr = new String[] {"男","女"};
    private TextView genderText;
    private TextView nickname;
    private TextView password;
    private TextView signature;
    private Button save;

    private String phone;
    private String oldnickname;
    private String oldpassword;
    private String oldsignature;
    private String oldsex;
    private String newsex;
    private String newnickname;
    private String newpassword;
    private String newsignature;

    private ProgressDialog progessDlg;
    private netRequest requestFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_edit_profile);

        Bundle data = getIntent().getExtras();
        phone= data.getString("phone");
        oldnickname=data.getString("nickname");
        oldpassword=data.getString("password");
        oldsignature=data.getString("signature");
        oldsex=data.getString("sex");

        requestFragment=new  netRequest(this,this);
        genderText = (TextView) findViewById(R.id.tv_gender);
        genderText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderDialog();
            }
        });
        save=(Button)findViewById(R.id.save_profile);
        nickname =(TextView)findViewById(R.id.tv_nickname);
        password =(TextView)findViewById(R.id.tv_password);
        signature=(TextView)findViewById(R.id.tv_signature);
        nickname.setText(oldnickname);
        password.setText("");
        signature.setText(oldsignature);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newnickname=nickname.getText().toString();
                newpassword=password.getText().toString();
                newsignature=signature.getText().toString();

                Map map = new HashMap();
                map.put("phone",phone);
                if(newpassword.length()==0)
                map.put("password" ,oldpassword);
                else
                map.put("password",newpassword);
                map.put("nickname",newnickname);
                map.put("school","nju");
                map.put("signature",newsignature);
                map.put("type", StatusCode.REQUEST_PROFILE_EDIT);
                requestFragment.httpRequest(map, CommonUrl.editProfile);
           //     progessDlg= ProgressDialog.show(EditProfile.this, "campusWall", "处理中", true, false);
            }
        });
    }

    /*
    性别选择框
     */
    private void showGenderDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this).
                setSingleChoiceItems(genderArr , 0 , new DialogInterface.OnClickListener(){  //0表示默认的位置，即“男”
                    @Override
                    public void onClick(DialogInterface dialog, int which) { //which表示选中的位置
                        genderText.setText(genderArr[which]);
                        dialog.dismiss(); //随便点击一个item，dialog消失，不用点击确认
                    }
                });

        alertDialog.show();
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if (requestUrl.equals(CommonUrl.editProfile)) {//返回登录请求
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));

            if (code == StatusCode.REQUEST_PROFILE_EDIT_SUCCESS) {
              //  progessDlg.cancel();
                Intent intent = new Intent(getApplicationContext(), profileActivity.class);
                Bundle data = new Bundle();
                data.putString("phone", phone);
                intent.putExtras(data);
                startActivity(intent);
                finish();
                return;
            } else {
              //  progessDlg.cancel();//进度条取消
                Toast.makeText(EditProfile.this, "保存失败", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    @Override
    public void exception(IOException e, String requestUrl){

    }
}
