package com.team.nju.campuswall.Activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.team.nju.campuswall.R;

public class showMessagesActivity extends FragmentActivity implements
        SearchView.OnQueryTextListener {
    private ListView list;
    private SearchView sv;
    private ListView lv;
    // 自动完成的列表
    private final String[] mStrings = {"aaaaa", "bbbbbb", "cccccc", "ddddddd"};

    private ViewPager vPager = null;
    /**
     * 代表选项卡下的下划线的imageview
     */
    private ImageView cursor = null;
    /**
     * 选项卡下划线长度
     */
    private static int lineWidth = 0;
    /**
     * 偏移量
     * （手机屏幕宽度/3-选项卡长度）/2
     */
    private static int offset = 0;
    /**
     * 选项卡总数
     */
    private static final int TAB_COUNT = 3;
    /**
     * 当前显示的选项卡位置
     */
    private int current_index = 0;
    /**
     * 选项卡标题
     */

    private TextView text1, text2, text3;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_messages);

        TextView t1=(TextView)findViewById(R.id.text1) ;
        TextView t2=(TextView)findViewById(R.id.text2) ;
        TextView t3=(TextView)findViewById(R.id.text3) ;
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vPager.setCurrentItem(0);
            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vPager.setCurrentItem(1);
            }
        });
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vPager.setCurrentItem(2);
            }
        });
        ImageView issue =(ImageView)findViewById(R.id.issue);
        ImageView profile =(ImageView) findViewById(R.id.profile);
        issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(showMessagesActivity.this,publishActivity.class);
               startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(showMessagesActivity.this,profileActivity.class);
                startActivity(intent);
            }
        });
        //    lv = (ListView) findViewById(R.id.lv);
        //   lv.setAdapter(new ArrayAdapter(this,android.R.layout.simple_list_item_1, mStrings));
        //   lv.setTextFilterEnabled(true);//设置lv可以被过虑
        sv = (SearchView) findViewById(R.id.sv);
        // 设置该SearchView默认是否自动缩小为图标
        sv.setIconifiedByDefault(false);
        // 为该SearchView组件设置事件监听器
        sv.setOnQueryTextListener(this);
        // 设置该SearchView显示搜索按钮
        sv.setSubmitButtonEnabled(true);
        // 设置该SearchView内默认显示的提示文本
        sv.setQueryHint("查找");

        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        final TextView[] titles = {text1, text2, text3};
        vPager =(ViewPager) findViewById(R.id.vPager);
        vPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return TAB_COUNT;
            }

            @Override
            public Fragment getItem(int index)//直接创建fragment对象并返回
            {

                switch (index) {
                    case 0:
                        return new tab1();
                    case 1:
                        return new tab2();
                    case 2:
                        return new tab3();
                }
                return null;
            }
        });
        vPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int one = offset * 2 + lineWidth;// 页卡1 -> 页卡2 偏移量

            @Override
            public void onPageSelected(int index)//设置标题的颜色以及下划线的移动效果
            {
//                Animation animation = new TranslateAnimation(one * current_index, one * index, 0, 0);
//                animation.setFillAfter(true);
//                animation.setDuration(300);
//                cursor.startAnimation(animation);
//                titles[current_index].setTextColor(Color.BLACK);
//                titles[index].setTextColor(Color.RED);
//                current_index = index;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int index) {
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

     //用户输入字符时激发该方法
    @Override
    public boolean onQueryTextChange(String newText) {
        Toast.makeText(showMessagesActivity.this, "textChange--->" + newText, Toast.LENGTH_SHORT).show();
        if (TextUtils.isEmpty(newText)) {
            // 清除ListView的过滤
            lv.clearTextFilter();
        } else {
            // 使用用户输入的内容对ListView的列表项进行过滤
            lv.setFilterText(newText);
        }
        return true;
    }

    // 单击搜索按钮时激发该方法
    @Override
    public boolean onQueryTextSubmit(String query) {
        // 实际应用中应该在该方法内执行实际查询
        // 此处仅使用Toast显示用户输入的查询内容
        Toast.makeText(this, "您的选择是:" + query, Toast.LENGTH_SHORT).show();
        return false;
    }

//    private void initImageView()
//    {
//        cursor = (ImageView) findViewById(R.id.cursor);
//        //获取图片宽度
//        lineWidth = BitmapFactory.decodeResource(getResources(),R.drawable.line).getWidth();
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        //获取屏幕宽度
//        int screenWidth = dm.widthPixels;
//        Matrix matrix = new Matrix();
//        offset = (int) ((screenWidth/(float)TAB_COUNT - lineWidth)/2);
//        matrix.postTranslate(offset, 0);
//        //设置初始位置
//        cursor.setImageMatrix(matrix);
//    }
}
