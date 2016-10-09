package com.team.nju.campuswall.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.team.nju.campuswall.Adapter.ListItemClickHelp;
import com.team.nju.campuswall.Adapter.messageListAdapter;
import com.team.nju.campuswall.Model.MessageModel;
import com.team.nju.campuswall.Network.NetworkCallbackInterface;
import com.team.nju.campuswall.Network.StatusCode;
import com.team.nju.campuswall.Network.netRequest;
import com.team.nju.campuswall.R;
import com.team.nju.campuswall.Util.CommonUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class concernedActivity extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface,ListItemClickHelp {
    private netRequest requestFragment;
    private List<MessageModel> messageModel ;
    TextView title;
    ListView lv;
    private int tempAcid;
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
        lv=(ListView)findViewById(R.id.concernedlist);
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
            {
                Map map = new HashMap();
                map.put("phone", phone);
                map.put("type", StatusCode.REQUEST_MY_PRODUCT);
                requestFragment.httpRequest(map, CommonUrl.search);
            }
                break;
            case "star":
                title.setText("我关注的");
            {
                Map map = new HashMap();
                map.put("phone", phone);
                map.put("type", StatusCode.REQUEST_MY_STAR);
                requestFragment.httpRequest(map, CommonUrl.search);
            }
                break;
            default:
                break;
        }
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        Message message = new Message();
        messageModel= new ArrayList<MessageModel>();
        //查询消息的返回
        if (requestUrl.equals(CommonUrl.search)) {
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code == StatusCode.REQUEST_MY_PRODUCT_SUCCESS||code == StatusCode.REQUEST_MY_STAR_SUCCESS) {
                JSONArray jsonArray = object.getJSONArray("contents");
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject dongTai = jsonArray.getJSONObject(i);
                    MessageModel itemModel = new MessageModel();
                    itemModel.setAccommentN((int)dongTai.get("AccommentN"));
                    itemModel.setAccontent((String) dongTai.get("Accontent"));
                    itemModel.setAcid((int) dongTai.get("Acid"));
                    itemModel.setAclikeN((int) dongTai.get("AclikeN"));
                    itemModel.setAcsponsorid((int)dongTai.get("Acsponsorid"));
                    itemModel.setAcsponsorname((String)dongTai.get("Acsponsorname"));
                    itemModel.setAcsponsT((String)dongTai.get("AcsponsT"));
                    itemModel.setActitle((String)dongTai.get("Actitle"));
                    itemModel.setIsliked((int) dongTai.get("Acisliked"));
                    itemModel.setAcsponsorimg((String)dongTai.get("Acsponsorimg"));
                    itemModel.setAcimgurl((String)dongTai.get("Acimgurl"));
                    itemModel.setNiming((int)dongTai.get("niming"));
                    itemModel.setAcsponsorid((int)dongTai.get("Acsponsorid"));
                    messageModel.add(itemModel);
                }
                message.what = StatusCode.REQUEST_SEARCH_SUCCESS;
                handler.sendMessage(message);
                return;
            } else {
                message.what = StatusCode.STATUS_ERROR;
                handler.sendMessage(message);
                return;
            }
        }
        //点赞请求的回复
        if(requestUrl.equals(CommonUrl.star)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code == StatusCode.REQUEST_ISSTAR) {                   //查询：赞过
                message.what = StatusCode.REQUEST_ISSTAR;
                handler.sendMessage(message);
                return;
            }else if(code == StatusCode.REQUEST_NOTSTAR){             //查询：没赞过
                message.what = StatusCode.REQUEST_NOTSTAR;
                handler.sendMessage(message);
                return;
            }else if(code == StatusCode.REQUEST_STAR_SUCCESS){       //进行点赞
                this.onResume();
                return;
            }else if(code == StatusCode.REQUEST_UNSTAR_SUCCESS){    //取消点赞
                this.onResume();
                return;
            }
            else{
                message.what = StatusCode.STATUS_ERROR;
                handler.sendMessage(message);
                return;
            }
        }
    }

    private List<Map<String,Object>> getData() {
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        for(int i=0;i<messageModel.size();i++){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("isLike",messageModel.get(i).isliked());
            map.put("id",messageModel.get(i).getAcid());
            map.put("title",messageModel.get(i).getActitle());
            map.put("content",messageModel.get(i).getAccontent());
            map.put("remarkNum",messageModel.get(i).getAccommentN());
            map.put("starNum",messageModel.get(i).getAclikeN());
            map.put("author",messageModel.get(i).getAcsponsorname());
            map.put("time",messageModel.get(i).getAcsponsT());
            map.put("userimg",messageModel.get(i).getAcsponsorimg());
            map.put("image",messageModel.get(i).getAcimgurl());
            map.put("niming",messageModel.get(i).getNiming());
            map.put("authorid",messageModel.get(i).getAcsponsorid());
            //     map.put("image",R.drawable.XXXX);  可以加头像
            list.add(map);
        }
        return list;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case StatusCode.REQUEST_SEARCH_SUCCESS: //成功返回所有动态
                {
                    List<Map<String,Object>> list = getData();
                    lv.setAdapter(new messageListAdapter(concernedActivity.this,list,concernedActivity.this));
                    break;
                }
                case StatusCode.REQUEST_ISSTAR:    //已经点赞，进行消赞的操作
                {
                    Map map = new HashMap();
                    map.put("type", StatusCode.REQUEST_UNSTAR);
                    map.put("acid", tempAcid);
                    map.put("phone", mainActivity.phone);
                    requestFragment.httpRequest(map, CommonUrl.star);
                }
                case StatusCode.REQUEST_NOTSTAR:  //没有赞过，进行点赞的操作
                {
                    Map map = new HashMap();
                    map.put("type", StatusCode.REQUEST_STAR);
                    map.put("acid", tempAcid);
                    map.put("phone", mainActivity.phone);
                    requestFragment.httpRequest(map, CommonUrl.star);
                }
                default: //用户身份认证失败
                {
                    Toast.makeText(concernedActivity.this, "网络错误", Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }
    };

    @Override
    public void onClick(View item, View widget, int position, int which, int id, int author) {
        tempAcid=id;
        switch (which) {
            case R.id.star:                 //点赞或者取消
                if(!(phone==null)) {
                    Map map = new HashMap();
                    map.put("type", StatusCode.REQUEST_ASK_ISSTAR);
                    map.put("acid", id);
                    map.put("phone", mainActivity.phone);
                    requestFragment.httpRequest(map, CommonUrl.star);
                }else
                    Toast.makeText(this, "登陆后进行更多操作", Toast.LENGTH_LONG).show();
                break;
            case R.id.remark:              //跳转评论界面
                if(!(phone==null)) {
                    Intent intent = new Intent(this, CommentActivity.class);
                    Bundle data = new Bundle();
                    data.putString("phone", phone);
                    data.putInt("id",id);
                    intent.putExtras(data);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "登陆后进行更多操作", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.authorpic:
                Intent intent = new Intent(concernedActivity.this,Profile_Others.class);
                Bundle data = new Bundle();
                data.putString("phone", phone);
                data.putInt("authorid",author);
                intent.putExtras(data);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
