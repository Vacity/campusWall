package com.team.nju.campuswall.Adapter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.team.nju.campuswall.Activity.signUpActivity;
import com.team.nju.campuswall.App;
import com.team.nju.campuswall.R;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/9/4.
 */
public class messageListAdapter extends BaseAdapter {

    private List<Map<String,Object>> data;
    private LayoutInflater layoutInflater;
    private ListItemClickHelp callback;
    private Context context;

    public  messageListAdapter(Context context,List<Map<String,Object>> data,ListItemClickHelp callback){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
        this.callback=callback;
    }

    public final class Zujian{
        public TextView title;
        public TextView author;
        public TextView time;
        public TextView content;
        public TextView remarkNum;
        public ImageView remark;
        public TextView starNum;
        public ImageView star;
        public ImageView photo;
        public ImageView image;
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
    public View getView(int i, View view, final ViewGroup viewGroup) {
        Zujian zujian = null;
        if (view == null) {
            zujian = new Zujian();
            view = layoutInflater.inflate(R.layout.message_list, null);
            zujian.author = (TextView) view.findViewById(R.id.author);
            zujian.time = (TextView) view.findViewById(R.id.time);
            zujian.title = (TextView) view.findViewById(R.id.title);
            zujian.content = (TextView) view.findViewById(R.id.content);
            zujian.remarkNum = (TextView) view.findViewById(R.id.remarkNum);
            zujian.starNum = (TextView) view.findViewById(R.id.starNum);
            zujian.remark = (ImageView) view.findViewById(R.id.remark);
            zujian.star = (ImageView) view.findViewById(R.id.star);
            zujian.photo = (ImageView) view.findViewById(R.id.authorpic);
            zujian.image = (ImageView) view.findViewById(R.id.contentimage);
            view.setTag(zujian);
        } else {
            zujian = (Zujian) view.getTag();
        }
        if ((int) data.get(i).get("niming") == 1) {
            zujian.author.setText("匿名用户");
            zujian.photo.setImageResource(R.drawable.photo);
        } else {
            zujian.author.setText((String) data.get(i).get("author"));
            if (data.get(i).get("userimg") == null)
                zujian.photo.setImageResource(R.drawable.photo);
            else {
                Glide.with(App.getContextObject())
                        .load((String) data.get(i).get("userimg")).centerCrop()
                        .into(zujian.photo);
            }
        }
        if (data.get(i).get("image") != null) {
            Glide.with(App.getContextObject())
                    .load((String) data.get(i).get("image")).centerCrop()
                    .into(zujian.image);
        }
        zujian.time.setText((String) data.get(i).get("time"));
        zujian.title.setText((String) data.get(i).get("title"));
        zujian.content.setText((String) data.get(i).get("content"));
        zujian.remarkNum.setText(Integer.toString((int) data.get(i).get("remarkNum")));
        zujian.starNum.setText(Integer.toString((int) data.get(i).get("starNum")));
        if (((int) data.get(i).get("isLike")) == 1)
            zujian.star.setImageResource(R.drawable.heart2);
        final int id = (int) data.get(i).get("id");
        final int p = i;
        final int one = zujian.star.getId();
        final int two = zujian.remark.getId();
        final int three = zujian.photo.getId();
        final View convertView = view;
        zujian.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(convertView, viewGroup, p, one, id);
            }
        });
        zujian.remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(convertView, viewGroup, p, two, id);
            }
        });
        if ((int) data.get(i).get("niming") == 0) {
            zujian.photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(convertView, viewGroup, p, three, id);
                }
            });
        }
            return view;
        }
}
