package com.harmy.lock;

/**
 * Description:
 * Created by za-hejun on 2019/3/14
 */
public class WorkJob implements Runnable{

    private Worker worker;

    private boolean addLock = true;

    public WorkJob(Worker worker, boolean addLock) {
        this.worker = worker;
        this.addLock = addLock;
    }

    public WorkJob(){};

    @Override
    public void run() {
        if(addLock) worker.doJobLock();
         worker.doJobUnLock();
    }
}
