package com.team.nju.campuswall.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
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

public class showSearchActivity extends AppCompatActivity implements ListItemClickHelp,NetworkCallbackInterface.NetRequestIterface,SearchView.OnQueryTextListener {

    private List<MessageModel> messageModel ;
    private netRequest requestFragment;
    private SearchView sv;
    private ListView lv;
    private String phone=mainActivity.phone;
    private String query;
    private String[] hot;
    private int tempAcid;
    private TextView cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_search);

        sv=(SearchView)findViewById(R.id.search);
        lv=(ListView) findViewById(R.id.searchresult);
        cancel = (TextView) findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        hot = loadData();
        lv.setAdapter(new ArrayAdapter<Object>(getApplicationContext(),
                android.R.layout.simple_expandable_list_item_1, hot));
        sv.setOnQueryTextListener(this);
        requestFragment=new  netRequest(this,this);
        int id = sv.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView)sv.findViewById(id);
        textView.setHintTextColor(Color.WHITE);
    }


    private String[] loadData() {
        String[] hot ={"aa","ab","bc","d"};
        return hot;
    }


    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        Message message = new Message();
        messageModel= new ArrayList<MessageModel>();
        //查询消息的返回
        if (requestUrl.equals(CommonUrl.search)) {
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code == StatusCode.REQUEST_SEARCH_SUCCESS) {
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
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }

    @Override
    public void onClick(View item, View widget, int position, int which, int id) {
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
            default:
                break;
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case StatusCode.REQUEST_SEARCH_SUCCESS: //成功返回所有动态
                {
                    List<Map<String,Object>> list = getData();
                    lv.setAdapter(new messageListAdapter(showSearchActivity.this,list,showSearchActivity.this));
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
                    Toast.makeText(showSearchActivity.this, "网络错误", Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }
    };

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
            //     map.put("image",R.drawable.XXXX);  可以加头像
            list.add(map);
        }
        return list;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
         Map map = new HashMap();
        map.put("type", StatusCode.REQUEST_ASK_SEARCH);
        map.put("searchurl",s);
        map.put("phone","phone");
        requestFragment.httpRequest(map, CommonUrl.search);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (TextUtils.isEmpty(s)) {
            // Clear the text filter.
            lv.clearTextFilter();
        } else {
            // Sets the initial value for the text filter.
            lv.setFilterText(s.toString());
        }
        return true;
    }
}
