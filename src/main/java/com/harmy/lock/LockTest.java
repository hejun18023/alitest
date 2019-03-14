package com.harmy.lock;

import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Created by za-hejun on 2019/3/14
 */
public class LockTest {
    public static void main(String[] args) throws InterruptedException {
        TicketSpinLock lock = new TicketSpinLock();
        Worker worker = new Worker(lock);
        for (int i = 0; i < 100; i++) {
            new Thread(new WorkJob(worker, true), i + "").start();
        }
    }

}
