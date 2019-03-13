import com.harmy.model.Quota;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Description:
 * Created by za-hejun on 2019/3/13
 */
public class DataAsyncAPI {
    public static LinkedBlockingQueue                     queue  = new LinkedBlockingQueue();
    public static Map<String, LinkedBlockingQueue<Quota>> quatas = new ConcurrentHashMap<>();

    /**
     * 存放需要异步记录的数据
     *
     * @param asyncInfo
     * @throws InterruptedException
     */
    public static void putDataList(List<? extends AsyncInfo> asyncInfoList) throws InterruptedException {
        if (null != asyncInfoList &&  asyncInfoList.size() > 0) {

            ArrayBlockingQueue<AsyncInfo> queue = getQueue(asyncInfoList.get(0));
            for (int i = 0; i < asyncInfoList.size(); i++) {
                // 往队尾插入对象
                queue.put(asyncInfoList.get(i));
            }
        }
    }

    /**
     * 存放需要异步记录的数据
     *
     * @param asyncInfo
     */
    public static void putData(AsyncInfo asyncInfo) {
        try {
            ArrayBlockingQueue<AsyncInfo> queue = getQueue(asyncInfo);

            // 往队尾插入对象
            queue.put(asyncInfo);
        } catch (Exception e) {
            logger.error("Async Write failed:" + e);
            e.printStackTrace();
        }

    }
}
