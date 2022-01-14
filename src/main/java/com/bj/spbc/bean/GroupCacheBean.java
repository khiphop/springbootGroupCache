package com.bj.spbc.bean;

import com.bj.spbc.helper.groupcache.GroupCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

// can't use fromObject()
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;

@Component
@PropertySource(value = {"classpath:config/group.properties"})
public class GroupCacheBean {

    public GroupCache<Integer> instance = null;

    @Value("${group.list}")
    private String groupList;

    @Bean
    public GroupCache<Integer> getInstance() {
        if (instance != null) {
            return instance;
        }

        System.out.println("new GroupCache");
        instance = new GroupCache<Integer>();

        registerGroups();

        return instance;
    }


    private void registerGroups() {
        JSONArray jsonArray = JSONArray.fromObject(groupList);
        for (int i = 0; i < jsonArray.size(); i++) {
            Object obj = jsonArray.get(i);

            JSONObject jsonObject = JSONObject.fromObject(obj);
            instance.registerGroup((String) jsonObject.get("group"), 0, (String) jsonObject.get("url"));
        }
    }

}
