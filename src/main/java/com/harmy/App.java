package com.harmy;

import com.harmy.producer.QuotaInitProducer;

import java.io.File;
import java.text.MessageFormat;
import java.util.concurrent.*;

/**
 * Description:
 * Created by za-hejun on 2019/3/14
 */
public class App 
{

    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    private static final String filePath = "D:\\tmp\\alitest";
    /**
     * 获取文件集合
     * @param filePath
     * @return
     */
    private static File[] getFiles(String Path){
        File fileDic = new File(Path);
        if(null == fileDic || !fileDic.isDirectory()){
            System.out.println(MessageFormat.format("file path error, path = {0}", Path));
            return null;
        }
        return fileDic.listFiles(file ->{
            return !isBlank(file.getName()) || file.getName().endsWith(".txt");
        });
    }


    public static void main(String[] args) {
        File[] files = getFiles(filePath);

        if(null != files && files.length > 0){

            //所有文件都被处理了之后才开始输出结果
            final CountDownLatch latch = new CountDownLatch(files.length);

            //先开启排序线程（消费）
            Thread consume = new Thread(()->{
                DataAsyncAPI.getInstance().putAndSort();
            });
            consume.start();
            for (File file : files) {
                try {
                    executor.submit(new QuotaInitProducer(file, latch));
                } catch (RejectedExecutionException e) {
                    try {
                        //线程池所有线程都在忙就休眠0.1S
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            try {
                latch.await();
                //等待排序完,kill掉线程
                while(DataAsyncAPI.getInstance().remainSize() > 0){
                    TimeUnit.MILLISECONDS.sleep(1000);
                }
                consume.interrupt();

                DataAsyncAPI.getInstance().printMin();
                DataAsyncAPI.getInstance().writeFile(filePath + "\\sort.txt");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }

    private static boolean isBlank(String str){
        if(null == str || str.trim().length() == 0){
            return true;
        }
        return false;
    }
}
