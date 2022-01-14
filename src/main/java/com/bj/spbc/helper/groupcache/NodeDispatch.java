package com.bj.spbc.helper.groupcache;

import com.alibaba.fastjson.JSONObject;
import com.bj.spbc.bean.GroupCacheBean;
import com.bj.spbc.helper.net.HttpHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Objects;

@Repository
public class NodeDispatch {

    @Autowired
    private HttpHelper httpHelper;

    @Autowired
    private GroupCacheBean groupCacheBean;


    private String selfNode;

    private String selfIp;

    private Integer selfPort;

    public NodeDispatch() {
    }

    public void registerSelfNode(String ip, int port) {
        selfIp = ip;
        selfPort = port;
        selfNode = ip + ":" + port;

        registerNode(selfNode);
    }

    public void setIp(String ip) {
        selfIp = ip;
    }

    public void setPort(int port) {
        selfPort = port;
    }

    public void registerNode(String node) {
        ConHash.registerNode(node);
    }

    public String getHandler(String group, String key, boolean inner) throws InterruptedException {
        String node = ConHash.chooseNode(key);
        System.out.println("chooseNode: " + node);

        if (!Objects.equals(node, selfNode)) {
            System.out.println("Trigger inner call");

            return innerGet(node, group, key);
        } else {
            GroupCache<Integer> groupCache = groupCacheBean.getInstance();

            return groupCache.get(group, key);
        }
    }


    public void setHandler(String group, String key, String val, boolean inner) throws InterruptedException {
        String node = ConHash.chooseNode(key);
        System.out.println("chooseNode: " + node);

        if (!Objects.equals(node, selfNode) && !inner) {
            System.out.println("Trigger inner call");

            innerSet(node, group, key, val);
        } else {
            GroupCache<Integer> groupCache = groupCacheBean.getInstance();

            groupCache.set(group, key, val);
        }
    }

    public String innerGet(String node, String group, String key) throws InterruptedException {
        String url = "http://" + node + "/cache/" + group + "/" + key;
        System.out.println(url);

        JSONObject json = httpHelper.get(url);

        if (Objects.equals(json, "")) {
            return "";
        }

        return (String) json.get("data");
    }

    public void innerSet(String node, String group, String key, String val) throws InterruptedException {
        String url = "http://" + node + "/cache";

        MultiValueMap<String, String> args = new LinkedMultiValueMap<>();
        args.add("group", group);
        args.add("key", key);
        args.add("val", val);

        httpHelper.post(url, args);
    }
}