package com.team.nju.campuswall.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.team.nju.campuswall.R;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/9/4.
 */
public class messageListAdapter extends BaseAdapter {

    private List<Map<String,Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;

    public  messageListAdapter(Context context,List<Map<String,Object>> data){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
    }

    public final class Zujian{
        public TextView title;
        public TextView content;
        public TextView remarkNum;
        public Button remark;
        public TextView starNum;
        public Button star;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Zujian zujian = null;
        if(view==null){
            zujian = new Zujian();
            view = layoutInflater.inflate(R.layout.message_list,null);
            zujian.title=(TextView)view.findViewById(R.id.title);
            zujian.content=(TextView)view.findViewById(R.id.content);
            zujian.remarkNum=(TextView)view.findViewById(R.id.remarkNum);
            zujian.starNum=(TextView)view.findViewById(R.id.starNum);
            zujian.remark=(Button)view.findViewById(R.id.remark);
            zujian.star=(Button)view.findViewById(R.id.star);
            view.setTag(zujian);
        }else{
            zujian=(Zujian)view.getTag();
        }
        zujian.title.setText((String)data.get(i).get("title"));
        zujian.content.setText((String)data.get(i).get("content"));
        zujian.remarkNum.setText((String)data.get(i).get("remarkNum"));
        zujian.starNum.setText((String)data.get(i).get("starNum"));
        return view;
    }
}
