package util;

import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class ZKUtil {

    private static ZooKeeper zk;
    private static DefaultWatch watch = new DefaultWatch();

    private static CountDownLatch initLatch = new CountDownLatch(1);
    public static ZooKeeper getZooKeeper(){
        try {
            zk = new ZooKeeper("127.0.0.1",1000,watch);
            watch.setCc(initLatch);
            initLatch.wait();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zk;
    }
}
