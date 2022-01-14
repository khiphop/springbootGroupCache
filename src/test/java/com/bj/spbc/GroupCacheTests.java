package com.bj.spbc;


import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GroupCache<K> {
//    private final int DefaultCacheCapacity;
//    private final int backSourceCd = 60;
//    private final HashMap<K, GroupStruct> groups;
//
//    /*
//    * @usage
//    * r.lock();
//    * r.unlock();
//    * */
//    private ReentrantLock mu = new ReentrantLock();
//
//    public GroupCache(int capacity) {
//        this.DefaultCacheCapacity = capacity;
//        groups = new HashMap<K, GroupStruct>();
//    }
//
//    static class GroupStruct {
//        Object key;
//        Object value;
//        int capacity;
//        LRUCache<Integer, String> lru;
//
//        public GroupStruct(int c) {
//            capacity = c;
//            lru = new LRUCache<Integer, String>(c, 0);
//        }
//    }
//
//
//    public void RegisterGroup(String name, int capacity) {
//        GroupStruct group = new GroupStruct(capacity);
//        groups.put((K) name, group);
//    }
//
//    public void set(String groupName, String name, String val) {
//        mu.lock();
//
//        GroupStruct g = groups.get(groupName);
//        if (g == null) {
//            mu.unlock();
//            return;
//        }
//
//        g.lru.set(name, val);
//
//        mu.unlock();
//    }
//
//    public String get(String groupName, String name) {
//        mu.lock();
//
//        GroupStruct g = groups.get(groupName);
//        if (g == null) {
//            mu.unlock();
//            return "";
//        }
//
//        String val = (String) g.lru.get(name);
//
//        mu.unlock();
//
//        return val;
//    }
//
//
//    public static void main(String[] args) {
//        GroupCache<Integer> groupCache = new GroupCache<Integer>(3);
//        groupCache.RegisterGroup("user", 0);
//        groupCache.set("user", "1", "a");
//        String r = groupCache.get("user", "1");
//        System.out.println(r);
//
//
////        LRUCache<Integer, String> lru = new LRUCache<Integer, String>(3, 0);
////
////        lru.set("1", "a");    // 1:a
////        System.out.println(lru.toString());
////        lru.set("2", "b");    // 2:b 1:a
////        System.out.println(lru.toString());
////        lru.set("3", "c");    // 3:c 2:b 1:a
////        System.out.println(lru.toString());
////        lru.set("4", "d");    // 4:d 3:c 2:b
////        System.out.println(lru.toString());
////        lru.set("1", "aa");   // 1:aa 4:d 3:c
////        System.out.println(lru.toString());
////        lru.set("2", "bb");   // 2:bb 1:aa 4:d
////        System.out.println(lru.toString());
////        lru.set("5", "e");    // 5:e 2:bb 1:aa
////        System.out.println(lru.toString());
////        lru.get("1");         // 1:aa 5:e 2:bb
////        System.out.println(lru.toString());
////        lru.remove("11");     // 1:aa 5:e 2:bb
////        System.out.println(lru.toString());
////        lru.remove("1");      //5:e 2:bb
////        System.out.println(lru.toString());
////        lru.set("1", "aaa");  //1:aaa 5:e 2:bb
////        System.out.println(lru.toString());
//    }
}
