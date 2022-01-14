package com.bj.spbc;

import java.util.HashMap;


class LRUCacheTest<K, V> {
    private final int CacheCapacity;
    private final int CacheBytes;
    private final HashMap<K, CacheNode> caches;
    private CacheNode first;
    private CacheNode last;

    public LRUCacheTest(int capacity, int bytes) {
        this.CacheCapacity = capacity;
        this.CacheBytes = bytes;

        caches = new HashMap<K, CacheNode>(capacity);
    }

    public void set(String k, String v) {
        CacheNode node = caches.get(k);

        if (node == null) {
            checkCapacity(k);

            node = new CacheNode();
            node.key = k;
        }

        node.value = v;
        moveToHead(node);
        caches.put((K) k, node);
    }

    private void checkCapacity(String k){
        if (CacheCapacity == 0) {
            return;
        }

        if (caches.size() < CacheCapacity) {
            return;
        }

        System.out.println("Trigger LruRemove: " + k);

        caches.remove(last.key);
        removeLast();
    }

    public Object get(String k) {
        CacheNode node = caches.get(k);

        if (node == null) {
            return null;
        }

        moveToHead(node);

        return node.value;
    }

    public void remove(String k) {
        CacheNode node = caches.get(k);
        if (node != null) {
            if (node.pre != null) {
                node.pre.next = node.next;
            }
            if (node.next != null) {
                node.next.pre = node.pre;
            }
            if (node == first) {
                first = node.next;
            }
            if (node == last) {
                last = node.pre;
            }
        }

        caches.remove(k);
    }

    public void clear() {
        first = null;
        last = null;
        caches.clear();
    }


    private void moveToHead(CacheNode node) {
        if (first == node) {
            return;
        }

        if (node.next != null) {
            node.next.pre = node.pre;
        }
        if (node.pre != null) {
            node.pre.next = node.next;
        }
        if (node == last) {
            last = last.pre;
        }
        if (first == null || last == null) {
            first = last = node;
            return;
        }

        node.next = first;
        first.pre = node;
        first = node;
        first.pre = null;
    }

    private void removeLast() {
        if (last != null) {
            last = last.pre;
            if (last == null) {
                first = null;
            } else {
                last.next = null;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        CacheNode node = first;
        while (node != null) {
            sb.append(String.format("%s:%s ", node.key, node.value));
            node = node.next;
        }

        return sb.toString();
    }

    static class CacheNode {
        CacheNode pre;
        CacheNode next;
        Object key;
        Object value;

        public CacheNode() {

        }
    }

    public static void main(String[] args)
    {
        LRUCacheTest<Integer, String> lru = new LRUCacheTest<Integer, String>(3, 0);

        lru.set("1", "a");    // 1:a
        System.out.println(lru.toString());
        lru.set("2", "b");    // 2:b 1:a
        System.out.println(lru.toString());
        lru.set("3", "c");    // 3:c 2:b 1:a
        System.out.println(lru.toString());
        lru.set("4", "d");    // 4:d 3:c 2:b
        System.out.println(lru.toString());
        lru.set("1", "aa");   // 1:aa 4:d 3:c
        System.out.println(lru.toString());
        lru.set("2", "bb");   // 2:bb 1:aa 4:d
        System.out.println(lru.toString());
        lru.set("5", "e");    // 5:e 2:bb 1:aa
        System.out.println(lru.toString());
        lru.get("1");         // 1:aa 5:e 2:bb
        System.out.println(lru.toString());
        lru.remove("11");     // 1:aa 5:e 2:bb
        System.out.println(lru.toString());
        lru.remove("1");      //5:e 2:bb
        System.out.println(lru.toString());
        lru.set("1", "aaa");  //1:aaa 5:e 2:bb
        System.out.println(lru.toString());
    }

}
