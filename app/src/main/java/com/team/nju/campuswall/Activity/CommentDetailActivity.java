package com.team.nju.campuswall.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class CommentDetailActivity extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface{
    private String phone = mainActivity.phone;
    private int id;
    private netRequest requestFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);

        final Button cancel = (Button) findViewById(R.id.tv_cancel);
        final Button send = (Button) findViewById(R.id.tv_send);
        final EditText commentContent = (EditText) findViewById(R.id.et_comment_content);

        Bundle data = getIntent().getExtras();
        id = data.getInt("id");
        requestFragment=new netRequest(this, this);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        send.setClickable(false);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel.setTextColor(getResources().getColor(R.color.colorPrimary));
//                Intent intent = new Intent(CommentDetailActivity.this, CommentActivity.class);
//                Bundle data = new Bundle();
//                data.putString("phone", phone);
//                data.putInt("id",id);
//                intent.putExtras(data);
//                startActivity(intent);
                finish();
            }
        });

        commentContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = commentContent.getText().toString();
                if (!content.equals("")) {
                    send.setClickable(true);
                    send.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    send.setClickable(false);
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 发送评论
                Log.d("id",Integer.toString(id));
                Map map = new HashMap();
                map.put("type", StatusCode.REQUEST_COMMENT);
                map.put("phone", phone);
                map.put("acid", id);
                map.put("comment", commentContent.getText().toString());
                requestFragment.httpRequest(map, CommonUrl.comment);
//                Intent intent = new Intent(CommentDetailActivity.this, CommentActivity.class);
//                Bundle data = new Bundle();
//                data.putString("phone", phone);
//                data.putInt("id",id);
//                intent.putExtras(data);
//                startActivity(intent);

            }
        });

    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if (requestUrl.equals(CommonUrl.comment)) {
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code == StatusCode.REQUEST_COMMENT_SUCCESS) {
                Intent intent = new Intent(CommentDetailActivity.this, CommentActivity.class);
                Bundle data = new Bundle();
                data.putString("phone", phone);
                data.putInt("id",id);
                intent.putExtras(data);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(CommentDetailActivity.this, "网络错误", Toast.LENGTH_LONG).show();
                return;
            }

        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }


}
