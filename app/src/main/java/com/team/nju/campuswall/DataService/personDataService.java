package com.team.nju.campuswall.DataService;

import com.team.nju.campuswall.Bean.personBean;

/**
 * Created by user on 2016/7/8.
 */
public interface personDataService {
    /*
    请求登录
     */
    boolean login(String username,String password);

    /*
    请求注册
     */
    boolean signup(personBean pb);
}
