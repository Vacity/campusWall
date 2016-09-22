package com.team.nju.campuswall.Activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.team.nju.campuswall.R;

public class EditProfile extends AppCompatActivity {
    private String[] genderArr = new String[] {"男","女"};
    private EditText genderText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        genderText = (EditText) findViewById(R.id.text_gender);
        genderText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderDialog();
            }
        });
    }

    /*
    性别选择框
     */
    private void showGenderDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(genderArr , 0 , new DialogInterface.OnClickListener(){  //0表示默认的位置，即“男”
                    @Override
                    public void onClick(DialogInterface dialog, int which) { //which表示选中的位置
                        genderText.setText(genderArr[which]);
                        dialog.dismiss(); //随便点击一个item，dialog消失，不用点击确认
                    }
                }
        );
        builder.show();
    }
}
