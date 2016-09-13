package com.team.nju.campuswall.Adapter;

import android.content.Context;
import com.team.nju.campuswall.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Senjoey on 16/09/08.
 */
public class CommentListAdapter extends BaseAdapter{

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    public CommentListAdapter(Context context, List<Map<String, Object>> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    /**
     * 组件集合,对应list.xml中的控件
     */
    public final class Widget {
        public ImageView photo;
        public TextView name;
        public Button starbtn;
        public TextView starNum;
        public TextView info;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    /**
     * 获得某一位置的数据
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    /**
     * 获得唯一标识
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Widget widget = null;
        if (convertView == null) {
            widget = new Widget();
            //获得组件,实例化组件
            convertView = layoutInflater.inflate(R.layout.comment_list, null);
            widget.photo = (ImageView) convertView.findViewById(R.id.personImage);
            widget.name = (TextView) convertView.findViewById(R.id.username);
            widget.starbtn = (Button) convertView.findViewById(R.id.starButton);
            widget.starNum = (TextView) convertView.findViewById(R.id.starNum);
            widget.info = (TextView) convertView.findViewById(R.id.commentInfo);
            convertView.setTag(widget);
        } else {
            widget = (Widget) convertView.getTag();
        }
        //绑定数据
//        widget.photo.setBackgroundResource((Integer)data.get(position).get("photo"));
        widget.name.setText((String)data.get(position).get("name"));
//        widget.starNum.setText((String)data.get(position).get("starNum"));
        widget.info.setText((String)data.get(position).get("info"));
        return convertView;
    }
}
