import distribute_lock.WatchCallBack;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.ZKUtil;

import java.util.concurrent.CountDownLatch;

public class LockTest {
    ZooKeeper zk;
    @Before
    public void connect(){
        zk = ZKUtil.getZooKeeper();

    }
    @After
    public void close(){
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testDistributeLock(){
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                //tryLock
                WatchCallBack watchCallBack = new WatchCallBack();
                watchCallBack.setZk(zk);
                watchCallBack.setCc(new CountDownLatch(1));
                String name = Thread.currentThread().getName();
                watchCallBack.setThreadName(name);
                watchCallBack.tryLock();
                //do something
                System.out.println(name+" do some thing");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //unlock
                watchCallBack.unLock();
            }).start();
        }
        while (true){

        }
    }
}
