package com.harmy.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 自旋锁：适应竞争不算激烈的情况，避免了线程的上下文切换；
 * 如果资源竞争激烈自旋时间会比较长，浪费CPU，这时候适用适用互斥锁
 * Created by za-hejun on 2019/3/14
 */
public class TicketSpinLock {
    private AtomicInteger ticketNum = new AtomicInteger(0);

    private AtomicInteger owner = new AtomicInteger(0);

    private static final ThreadLocal<Integer> local = new ThreadLocal<Integer>();

    public void lock() {
        int ticket = ticketNum.getAndIncrement();// 票据
        local.set(ticket);

        //每个线程如果没获取到锁的情况就自旋
        while (ticket != owner.get()) {

        }
    }

    public void unlock() {
        int ticket = local.get();

        //解锁，将owner自增1，这样所有循环的线程都有可能获取到该锁
        owner.compareAndSet(ticket, ticket + 1);
    }

}

