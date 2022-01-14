package com.bj.spbc.helper.groupcache;

import java.util.*;

public class ConHash {


    // {hashVal: ip:port}
    private static SortedMap<Integer, String> sortedMap = new TreeMap<Integer, String>();
    private static int virtualNodeCount = 10;

    public static void registerNode(String node) {
        addNode(node);

        System.out.println(sortedMap);
    }


    private static void addNode(String node) {
        for (int i = 0; i <= virtualNodeCount; i++) {
            String nodeStr = node + "#" + i;

            int hashVal = hashHandler(nodeStr);

            System.out.println("[" + node + "]  Hash Val:" + hashVal);
            sortedMap.put(hashVal, nodeStr);

        }
    }


    public static int hashHandler(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }


    public static String chooseNode(String key) {
        String nodeStr;
        int hashVal = hashHandler(key);

        // Find all element which > hashVal in Map
        SortedMap<Integer, String> subMap = sortedMap.tailMap(hashVal);
        if (subMap.isEmpty()) {
            // Choose the first one when no one > hashVal
            Integer i = sortedMap.firstKey();
            nodeStr = sortedMap.get(i);
        } else {
            Integer i = subMap.firstKey();
            nodeStr = subMap.get(i);
        }

        String[] splitRes=nodeStr.split("#");

        return splitRes[0];
    }

}
