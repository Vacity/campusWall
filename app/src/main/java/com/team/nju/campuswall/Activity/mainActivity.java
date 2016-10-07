package com.team.nju.campuswall.Activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.team.nju.campuswall.Network.NetworkCallbackInterface;
import com.team.nju.campuswall.Network.StatusCode;
import com.team.nju.campuswall.Network.netRequest;
import com.team.nju.campuswall.R;
import com.team.nju.campuswall.Util.CommonUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class mainActivity extends FragmentActivity {

    private Toolbar toolbar;
    private TextView isLog;
    private int flag = 0;
    public static String phone;


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
        cursor = (ImageView) findViewById(R.id.cursor);
        initImageView();
        toolbar=(Toolbar)findViewById(R.id.maintoolbar);
        toolbar.inflateMenu(R.menu.main_toolbar);
        //toolbar.setLogo();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                switch (menuItemId) {
                    case R.id.menu_search: {
                        Intent intent = new Intent(mainActivity.this,showSearchActivity.class);
                        startActivity(intent);
                    }break;
                    case R.id.menu_profile: {
                        if (phone == null)
                            Toast.makeText(mainActivity.this, "登陆后再查看吧", Toast.LENGTH_LONG).show();
                        else {
                            Intent intent = new Intent(mainActivity.this, profileActivity.class);
                            Bundle data = new Bundle();
                            data.putString("phone", phone);
                            intent.putExtras(data);
                            startActivity(intent);
                        }break;
                    }
                    case R.id.menu_publish: {
                        if (phone == null)
                            Toast.makeText(mainActivity.this, "登陆后才可以发布噢", Toast.LENGTH_LONG).show();
                        else {
                            Intent intent = new Intent(mainActivity.this, publishActivity.class);
                            Bundle data = new Bundle();
                            data.putString("phone", phone);
                            intent.putExtras(data);
                            startActivity(intent);
                        }break;
                    }
                }
                return true;
            }
        });

        isLog =(TextView)findViewById(R.id.log_state);
        Bundle data = getIntent().getExtras();
        if(data!=null)
        phone= data.getString("phone");
        if(phone==null) {
            isLog.setText("点击登录");
            isLog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mainActivity.this,LoginActivity.class);
                    startActivity(intent);

                }
            });
        }
        else {
            isLog.setVisibility(View.INVISIBLE);
        }

        TextView t1=(TextView)findViewById(R.id.text1) ;
        TextView t2=(TextView)findViewById(R.id.text2) ;
        TextView t3=(TextView)findViewById(R.id.text3) ;
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vPager.setCurrentItem(0);
                flag=0;
            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vPager.setCurrentItem(1);
                flag=1;
            }
        });
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vPager.setCurrentItem(2);
                flag=2;
            }
        });

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
                Animation animation = new TranslateAnimation(one * current_index, one * index, 0, 0);
                animation.setFillAfter(true);
                animation.setDuration(300);
                cursor.startAnimation(animation);
                titles[current_index].setTextColor(Color.GRAY);
                titles[index].setTextColor(0xFFaaefc5);
                current_index = index;
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

    private void initImageView()
    {

        //获取图片宽度
        lineWidth = BitmapFactory.decodeResource(getResources(),R.drawable.line).getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //获取屏幕宽度
        int screenWidth = dm.widthPixels;
        Matrix matrix = new Matrix();
        offset = (int) ((screenWidth/(float)TAB_COUNT - lineWidth)/2);
        matrix.postTranslate(offset, 0);
        //设置初始位置
        cursor.setImageMatrix(matrix);
    }
}
