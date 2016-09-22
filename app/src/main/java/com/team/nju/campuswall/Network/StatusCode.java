package com.team.nju.campuswall.Network;

/**
 * Created by user on 2016/9/16.
 */
public interface StatusCode {
    int STATUS_ERROR= 0;
    int STATUS_LOGIN=10100;
    int STATUS_REGISTER=10000;

    //请求获取验证码
    int REQUEST_REGISTER_VERIFYA=10001;
    //请求验证验证码
    int REQUEST_REGISTER_VERIFYB=10002;
    //请求注册
    int REQUEST_REGISTER=10002;
    //注册请求的成功返回码
    int RECIEVE_REGISTER_SUCCESS=10003;

    //登录的请求码
    int REQUEST_LOGIN=10010;
    //登录请求类别成功的返回码
    int REQUEST_LOGIN_SUCCESS =10011;

    //查看信息
    int REQUEST_MESSAGE=0;
    int REQUEST_CREATE_MESSAGE=10040;


    int REQUEST_PROFILE =10020;         //查看个人主页
    int REQUEST_PROFILE_SUCCESS =10021;     //查看个人主页
    int REQUEST_PROFILE_ERROR = 10022; //服务器错误

    int REQUEST_PROFILE_EDIT =10030;
    int REQUEST_PROFILE_EDIT_SUCCESS =10031;
    int REQUEST_PROFILE_EDIT_ERROR =10032;

    //请求我关注的用户（我的关注）
    int REQUEST_INFO_FOLLOWING = 10403;
    int REQUEST_FOLLOWING_SUCCESS = 10430; //请求我的关注成功
    int REQUEST_FOLLOWING_NONE = 10431; //用户没有关注任何人
    int REQUEST_USER_ILLEGAL = 10412; //用户不合法

    //请求取消关注
    int REQUEST_CANCEL_FOLLOWING = 10402;
    int REQUEST_CANCEL_SUCCESS = 10420; //请求取消关注成功
    int REQUEST_CANCEL_NONE = 10421; //未关注该用户

    //请求关注
    int REQUEST_FOLLOW_USER = 10401;
    int REQUEST_FOLLOW_SUCCESS = 10411; //请求关注成功


}
