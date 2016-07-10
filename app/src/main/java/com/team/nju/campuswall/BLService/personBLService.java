package com.team.nju.campuswall.BLService;

import com.team.nju.campuswall.Bean.personBean;

/**
 * Created by user on 2016/7/8.
 */
public interface personBLService {
    /*
    检测账号密码是否能够登录
     */
    boolean login(String account,String password);

    /*
    请求注册
     */
    boolean signUp(personBean pb);
}
