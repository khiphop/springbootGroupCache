package com.bj.spbc.helper.groupcache;
import com.alibaba.fastjson.JSONObject;
import com.bj.spbc.helper.net.HttpHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;


public class GroupCache<K> {

    @Autowired
    private HttpHelper httpHelper;

    private final int backSourceCd = 60000;
    private final HashMap<K, GroupStruct> groups;
    private HashMap<String, Long> backSourceCdMap;

    /*
     * @usage
     * r.lock();
     * r.unlock();
     * */
    private ReentrantLock mu = new ReentrantLock();

    public GroupCache() {
        groups = new HashMap<K, GroupStruct>();
        backSourceCdMap = new HashMap<String, Long>();
    }

    static class GroupStruct {
        int capacity;
        String backSourceUrl;
        CoreCache coreCache;

        public GroupStruct(int c, String bsu) {
            capacity = c;
            backSourceUrl = bsu;

            coreCache = new CoreCache(c);
        }
    }


    public void registerGroup(String name, int capacity, String backSourceUrl) {
        System.out.println("registerGroup " + name + ":" + backSourceUrl);
        GroupStruct group = new GroupStruct(capacity, backSourceUrl);
        groups.put((K) name, group);
    }

    public void set(String groupName, String key, String val) {
        GroupStruct g = groups.get(groupName);
        if (g == null) {
            return;
        }

        g.coreCache.set(key, val);
    }

    public String get(String groupName, String key) throws InterruptedException {
        GroupStruct g = groups.get(groupName);
        if (g == null) {
            return "";
        }

        String val = (String) g.coreCache.get(key);

        if (val == null || val.equals("")) {
            System.out.println("Trigger BackSource " + groupName + ":" + key);
            mu.lock();

            val = onBackSource(g, groupName, key);

            mu.unlock();
        }

        return val;
    }

    private String onBackSource(GroupStruct g, String groupName, String key) throws InterruptedException {
        String val = (String) g.coreCache.get(key);

        if (!Objects.equals(val, "")) {
            return val;
        }

        String tempIndex = getBackSourceCdMapKey(groupName, key);
        if (backSourceCdMap != null && backSourceCdMap.containsKey(tempIndex)) {
            Long cd = backSourceCdMap.get(tempIndex);

            if (backSourceCdMap.containsKey(tempIndex) && cd > System.currentTimeMillis()) {
                System.out.println("backSourceCd " + groupName + ": " + key);

                return "";
            }
        }

        // 回源开始 / indeed start back source
        String resVal = doBackSource(g, groupName, key);
        if (!Objects.equals(resVal, "")) {
            set(groupName, key, resVal);
        }

        return resVal;
    }

    public String doBackSource(GroupStruct g, String groupName, String key) {
        String tempIndex = getBackSourceCdMapKey(groupName, key);

        MultiValueMap<String, String> args = new LinkedMultiValueMap<>();
        args.add("group", groupName);
        args.add("key", key);

        System.out.println(g.backSourceUrl);

        String rs = httpHelper.post(g.backSourceUrl, args);
        backSourceCdMap.put(tempIndex, (System.currentTimeMillis() + backSourceCd));

        System.out.println(System.currentTimeMillis() + backSourceCd);

        if (Objects.equals(rs, "")){
            return "";
        }

        //JSONObject transfer string to JSONObject type
        JSONObject json = JSONObject.parseObject(rs);

        return (String) json.get("msg");
    }


    public static String getBackSourceCdMapKey(String groupName, String key) {
        return md5Handle(groupName) + "--" + md5Handle(key);
    }


    public static String md5Handle(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] byteDigest = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            // 32bit encode
            return buf.toString();
            // 16bit encode
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}