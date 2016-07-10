package com.team.nju.campuswall.Data;

import com.team.nju.campuswall.Bean.personBean;
import com.team.nju.campuswall.DataService.personDataService;

/**
 * Created by user on 2016/7/8.
 */
public class personDataImpl implements personDataService {
    @Override
    public boolean login(String username, String password) {
        return false;
    }

    @Override
    public boolean signup(personBean pb) {
        return false;
    }
}
