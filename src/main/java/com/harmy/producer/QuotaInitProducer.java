package com.harmy.producer;

import com.harmy.model.Quota;

import java.io.*;

/**
 * Description:
 * Created by za-hejun on 2019/3/13
 */
public class QuotaInitProducer implements Runnable {

    private File file;

    public QuotaInitProducer(){}

    public QuotaInitProducer(File file){
        this.file = file;
    }

    @Override
    public void run() {
        FileInputStream is = null;
        BufferedReader bufferedReader = null;
        try {
            is = new FileInputStream(file);
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            String str = null;
            while((str = bufferedReader.readLine()) != null) {
                String[] strs = str.split(",");
                if(null != strs && strs.length != 3) continue;
                Quota quota = new Quota(strs[0], strs[1], Float.parseFloat(strs[2]));

            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
