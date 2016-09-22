package com.team.nju.campuswall.Util;

/**
 * Created by Senjoey on 16/09/16.
 */
public class CommonUrl {
    private static final String netUrl="http://121.42.54.202:800/";

    public static final String url = netUrl;
    public static final String imageUrl ="xxxx";
    public static final String loginAccount= url + "user/login";//登录接口，登陆之后才能更换头像、发表主题
    public static final String registerAccount = url + "user/regist";//注册申请验证码接口/注册验证验证码接口/注册提交信息接口（最终注册接口）

    public static final String getInfo = url + "user/getprofile";
    public static final String getProfile =url+"user/editprofile";


    public static final String publish = url + "publish";//获取关注和粉丝
    public static final String getFavorInfo = url + "user/favorite"; //获取收藏


}
