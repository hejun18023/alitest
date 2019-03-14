package com.harmy;

import com.harmy.model.Quota;
import com.harmy.util.TreeList;
import sun.awt.SunHints;

import java.io.*;
import java.text.CollationKey;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Created by za-hejun on 2019/3/14
 */
public class DataAsyncAPI {

    private LinkedBlockingQueue<Quota> queue;

    private TreeList<String, Quota> quotaSorts;

    private DataAsyncAPI(){
        queue  = new LinkedBlockingQueue();
        quotaSorts = new TreeList<>(
                (String groupId1, String groupId2) -> groupId1.compareTo(groupId2),
                (Quota q1, Quota q2) -> (int) (q1.getQuota() - q2.getQuota()));
    }

    private static class SingletonHolder{
        private static final DataAsyncAPI INSTANCE = new DataAsyncAPI();
    }

    public static DataAsyncAPI getInstance(){
        return SingletonHolder.INSTANCE;
    }


    /**
     * 存放需要异步记录的数据
     *
     * @param asyncInfo
     */
    public void putData(Quota quota) {
        try {
            queue.put(quota);
        } catch (Exception e) {
            System.out.println("producer produce data error." + e.getMessage());
        }
    }

    public Quota getData(){
        try {
            return queue.poll();
        } catch (Exception e) {
            System.out.println("producer get data error." + e.getMessage());
            return null;
        }
    }

    public int remainSize(){
        return queue.size();
    }

    public void putAndSort(){
        while(true){
            if(queue.size() > 0){
                Quota quota = queue.poll();
                quotaSorts.put(quota.getGroupId(), quota);
            }else{
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void printMin(){
        quotaSorts.forEach((key, value) ->{
            System.out.println(value.toString());
        });
    }

    public void writeFile(String path){
        FileWriter writer = null;
        final BufferedWriter rw;
        try {
            writer = new FileWriter(path);
            rw = new BufferedWriter(writer);
            quotaSorts.forEachNode(quota -> {
                try {
                    rw.write(quota.toString() + "\t\n");
                } catch (IOException e) {
                    System.out.println("" + e.getMessage());
                }
            });
            rw.close();
        } catch (IOException e) {
            System.out.println("" + e.getMessage());
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                System.out.println("" + e.getMessage());
            }
        }

    }


}
