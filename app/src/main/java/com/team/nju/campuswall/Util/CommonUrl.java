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

    public static final String getProfile = url + "user/getprofile";
    public static final String editProfile =url+"user/editprofile";


    public static final String publish = url + "activity/publish";//发布动态
    public static final String getMessage = url + "activity/ask"; //获取消息

    public static final String star = url + "activity/operation01";//点赞或者取消点赞

    public static final String comment = url + "activity/comment";//评论

    public static final String search = url + "activity/search";//搜索
}
