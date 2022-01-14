package com.bj.spbc.dao.cache.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserCache {


    private static final String KEY_PREFIX = "user:online:";

    public String getKey(Integer userId) {
        return KEY_PREFIX + userId;
    }

    // demo
    public void set() {
    }

    public String get(Integer userId) {
        return "";
    }
}
