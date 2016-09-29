package com.team.nju.campuswall.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.team.nju.campuswall.R;

public class CommentDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final TextView cancel = (TextView) findViewById(R.id.tv_cancel);
        final TextView send = (TextView) findViewById(R.id.tv_send);
        final EditText commentContent = (EditText) findViewById(R.id.et_comment_content);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel.setTextColor(getResources().getColor(R.color.colorPrimary));
                //TODO 返回显示评论的界面
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
                    send.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 发送评论
            }
        });

    }



}
