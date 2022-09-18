package distribute_lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class WatchCallBack implements Watcher, AsyncCallback.StringCallback, AsyncCallback.Children2Callback, AsyncCallback.StatCallback {
    ZooKeeper zk;
    String threadName;
    CountDownLatch cc = new CountDownLatch(1);
    String pathName;

    public CountDownLatch getCc() {
        return cc;
    }

    public void setCc(CountDownLatch cc) {
        this.cc = cc;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public void process(WatchedEvent event) {
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                zk.getChildren("/",false,this,"ctx");
;
                break;
            case NodeDataChanged:
                break;
            case NodeChildrenChanged:
                break;
        }

    }
    public void tryLock(){
        try {
            zk.create("/lock",
                    threadName.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL,
                    this,"ctx");
            cc.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void unLock(){
        try {
            zk.delete(pathName,-1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        //create回调
        if(name != null){
            pathName = name;
            System.out.println(threadName+"create node "+name);
            zk.getChildren("/",false,this,"ctx");
        }
    }

    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        //getChildren回调
        Collections.sort(children);

        int i = children.indexOf(pathName.substring(1));
        if(i == 0){
            System.out.println(threadName+"get Lock");
            cc.countDown();
        }else{
            zk.exists("/"+children.get(i-1),this,this,"ctx");
        }
    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {

    }
}
