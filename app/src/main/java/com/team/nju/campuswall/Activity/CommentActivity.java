package com.team.nju.campuswall.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.team.nju.campuswall.Adapter.CommentListAdapter;
import com.team.nju.campuswall.Adapter.messageListAdapter;
import com.team.nju.campuswall.Model.CommentModel;
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
import java.util.Objects;

public class CommentActivity extends Activity implements NetworkCallbackInterface.NetRequestIterface{

    private ListView listView = null;
    private String phone = mainActivity.phone;
    private int id;
    private netRequest requestFragment;
    private List<CommentModel> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        final Button comment = (Button) findViewById(R.id.tv_comment);
        final Button ret = (Button) findViewById(R.id.bt_exit);
        listView = (ListView) findViewById(R.id.commentList);
        requestFragment=new netRequest(this, this);
        List<Map<String, Object>> list = getData();
        Bundle data = getIntent().getExtras();
        id = data.getInt("id");
        if (list == null) {
            comment();
        } else {
            listView.setAdapter(new CommentListAdapter(this, list));
            commentList = new ArrayList<CommentModel>();

            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comment();
                }
            });
        }

        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void comment() {
        Intent intent = new Intent(CommentActivity.this, CommentDetailActivity.class);
        Bundle data = new Bundle();
        data.putString("phone", phone);
        data.putInt("id",id);
        intent.putExtras(data);
        startActivity(intent);
    }

    private void initInfo() {
        Map map = new HashMap();

        map.put("acid",id);
        map.put("type", StatusCode.REQUEST_ASK_COMMENT);
        requestFragment.httpRequest(map, CommonUrl.comment);
    }

    @Override
    public void onResume(){
        super.onResume();
        initInfo();
    }

    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        if (commentList == null) {
            return null;
        }
        for (int i = 0; i < commentList.size(); i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("commentid", commentList.get(i).getCommentid());
            map.put("comertel", commentList.get(i).getComertel());
            map.put("comcontent", commentList.get(i).getComcontent());
            list.add(map);
        }
        return list;
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        Message message = new Message();
        if (requestUrl.equals(CommonUrl.comment)) {
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code == StatusCode.REQUEST_ASK_COMMENT_SUCCESS) {
                JSONArray jsonArray = object.getJSONArray("contents");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject comment = jsonArray.getJSONObject(i);
                    CommentModel itemModel = new CommentModel();
                    itemModel.setCommentid((int) comment.get("Commentid"));
                    itemModel.setComertel((String) comment.get("Comertel"));
                    itemModel.setComcontent((String) comment.get("Comcontent"));
                }
                message.what = StatusCode.REQUEST_ASK_COMMENT_SUCCESS;
                handler.sendMessage(message);
                return;
            } else {
                message.what = StatusCode.REQUEST_ASK_COMMENT_ERROR;
                handler.sendMessage(message);
                return;
            }

        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }

    //服务器返回响应之后要对界面的操作由于不能干扰主UI线程，交给handler传递消息到主线程
   private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == StatusCode.REQUEST_ASK_COMMENT_ERROR) {
                List<Map<String, Object>> list = getData();
                listView.setAdapter(new CommentListAdapter(CommentActivity.this, list));
            } else if (msg.what == StatusCode.REQUEST_ASK_COMMENT_ERROR){
                Toast.makeText(CommentActivity.this, "网络错误", Toast.LENGTH_LONG).show();
            }
        }
    };

}
