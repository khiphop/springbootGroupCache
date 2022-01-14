package com.bj.spbc.helper.groupcache;


import java.util.HashMap;


class LRUCache<T, V> {
    private final int CacheCapacity;
    private final int CacheBytes;
    private final HashMap<T, CacheNode> caches;
    private CacheNode first;
    private CacheNode last;

    public LRUCache(int capacity, int bytes) {
        this.CacheCapacity = capacity;
        this.CacheBytes = bytes;

        caches = new HashMap<T, CacheNode>(capacity);
    }

    public void set(String k, String v) {
        CacheNode node = caches.get(k);

        if (node == null) {
            checkCapacity((T) k);

            node = new CacheNode();
            node.key = k;
        }

        node.value = v;
        moveToHead(node);
        caches.put((T) k, node);
    }

    private void checkCapacity(T k){
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

    public void remove(T k) {
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

        // should not delete although it displays gray in ide.
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
}

