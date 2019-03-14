package com.harmy.lock;

/**
 * Description:
 * Created by za-hejun on 2019/3/14
 */
public class Worker {
    private int i;

    private TicketSpinLock ticketLock;

    public Worker() {
    }

    public Worker(TicketSpinLock ticketLock) {
        this.ticketLock = ticketLock;
    }

    // 加锁
    public void doJobLock() {
        ticketLock.lock();
        i = i + 1;
        System.out.println(String.format("i=%s", i));
        ticketLock.unlock();
    }

    // 不加锁
    public void doJobUnLock() {
        i = i + 1;
        System.out.println(String.format("i=%s", i));
    }
}
