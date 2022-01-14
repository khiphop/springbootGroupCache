package com.bj.spbc.helper.groupcache;
import java.util.concurrent.locks.ReentrantLock;

public class CoreCache {
    private LRUCache<Integer, String> lru;

    /*
    * @usage
    * r.lock();
    * r.unlock();
    * */
    private ReentrantLock mu = new ReentrantLock();

    public CoreCache(int c) {
        lru = new LRUCache<Integer, String>(c, 0);
    }


    public void set(String key, String val) {
        mu.lock();

        lru.set(key, val);

        mu.unlock();
    }

    public String get(String key) throws InterruptedException {
        mu.lock();

        /*System.out.println("=============");
        System.out.println("get");
        System.out.println("get");
        Thread.sleep(1000);
        System.out.println("get");
        System.out.println("get");
        System.out.println("=============");*/

        String val = (String) lru.get(key);

        mu.unlock();

        if (val == null) {
            val = "";
        }

        return val;
    }


}
