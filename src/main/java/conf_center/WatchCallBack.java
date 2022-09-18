package conf_center;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class WatchCallBack implements Watcher, AsyncCallback.StatCallback,AsyncCallback.DataCallback {

    ZooKeeper zk;
    MyConf conf;
    CountDownLatch cc = new CountDownLatch(1);

    public void setConf(MyConf conf) {
        this.conf = conf;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }
    public void aWait(){
        zk.exists("/AppConf",this, this,"ABC");
        try {
            cc.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //Watch
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case None:
                break;
            case NodeCreated:
                zk.getData("/AppConf",this,this,"sad");
;
                break;
            case NodeDeleted:
                //cc = new CountDownLatch(1);
                conf.setConf("invalid");
                aWait();
                //aWait();
;
                break;
            case NodeDataChanged:
                zk.getData("/AppConf",this,this,"sad");
                break;
            case NodeChildrenChanged:
                break;
        }
    }
    //DataCallBack
    public void processResult(int i, String path, Object ctx, byte[] data, Stat stat) {
        if(data != null){
            String s = new String(data);
            conf.setConf(s);
            cc.countDown();
        }
    }
    //StatCallBack
    public void processResult(int i, String s, Object o, Stat stat) {
        if(stat != null){
            zk.getData("/AppConf",this,this,"StatCallBack");
        }
    }
}
