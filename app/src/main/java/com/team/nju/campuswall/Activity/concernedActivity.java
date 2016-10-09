package com.team.nju.campuswall.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.team.nju.campuswall.Network.NetworkCallbackInterface;
import com.team.nju.campuswall.Network.netRequest;
import com.team.nju.campuswall.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;


public class concernedActivity extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface{
    private netRequest requestFragment;
    TextView title;
    ListView list;
    Button exit;
    String phone,info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concerned);
        requestFragment= new netRequest(this, this);
        Bundle data = getIntent().getExtras();
        phone= data.getString("phone");
        info=data.getString("info");
        title=(TextView)findViewById(R.id.text_title);
        list=(ListView)findViewById(R.id.concernedlist);
        exit = (Button)findViewById(R.id.bt_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initInfo();
    }

    private void initInfo() {
        switch(info){
            case "product":
                title.setText("我发布的");

                break;
            case "join":
                title.setText("我参与的");

                break;
            case "star":
                title.setText("我关注的");

                break;
            default:
                break;
        }
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {

    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
