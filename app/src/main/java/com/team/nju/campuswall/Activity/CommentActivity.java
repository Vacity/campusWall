package com.team.nju.campuswall.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.team.nju.campuswall.Adapter.CommentListAdapter;
import com.team.nju.campuswall.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentActivity extends Activity {

    private ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        listView = (ListView) findViewById(R.id.commentList);
        List<Map<String, Object>> list = getData();
        listView.setAdapter(new CommentListAdapter(this, list));

    }

    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
//            map.put("photo", R.drawable.logo);
            map.put("name", "名字"+i);
//            map.put("starNum", i*10+"");
            map.put("info", "评论                     ");
            list.add(map);
        }
        return list;
    }
}
