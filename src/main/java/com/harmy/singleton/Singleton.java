package com.harmy.singleton;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description:
 * Created by za-hejun on 2019/3/14
 */
public class Singleton {
    private volatile static Singleton instance;

    private Singleton() {
    }

    //get the instance
    public static Singleton getInstance() {
        if (null == instance) {
            synchronized (Singleton.class) {
                if (null == instance) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
