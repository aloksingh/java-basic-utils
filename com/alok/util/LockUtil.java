package com.alok.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LockUtil {
    private static final ConcurrentMap<String, Holder> locks = new ConcurrentHashMap<String, Holder>();
    protected static final int HIGH_WATER_MARK = 100*1000;
    protected static final int LOW_WATER_MARK = 50*1000;
    private static final Object trimLock = new Object();

    public static String intern(String s) {
        if(locks.size() > HIGH_WATER_MARK){
            trimToSize(LOW_WATER_MARK);
        }
        Holder result = locks.get(s);
        if (result == null) {
            result = locks.putIfAbsent(s, new Holder(s));
            if (result == null)
                result = locks.get(s);
        }
        return locks.get(s).getLock();
    }

    private static void trimToSize(int lowWaterMark) {
        synchronized (trimLock){
            long minLastAccessTime = System.currentTimeMillis() - (1000*30);
            while(locks.size() > lowWaterMark){
                List<String> keysToRemove = new ArrayList<String>();
                for (Holder holder : locks.values()) {
                    if(holder.getAccessTime() < minLastAccessTime){
                        keysToRemove.add(holder.lock);
                    }
                }
                for (String s : keysToRemove) {
                    locks.remove(s);
                }
                minLastAccessTime = minLastAccessTime + 1000;
            }

        }
    }

    private static class Holder{
        private long accessTime;
        private final String lock;

        public Holder(String lock){
            this.lock = new String(lock);
            this.accessTime = System.currentTimeMillis();
        }

        public long getAccessTime() {
            return accessTime;
        }

        public String getLock() {
            this.accessTime = System.currentTimeMillis();
            return lock;
        }

        public void setAccessTime(long accessTime) {
            this.accessTime = accessTime;
        }
    }
}

