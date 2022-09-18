import conf_center.MyConf;
import conf_center.WatchCallBack;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.ZKUtil;

public class ConfigTest {
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
    public void getConf(){
        WatchCallBack watchCallBack = new WatchCallBack();
        watchCallBack.setZk(zk);
        //配置信息,在别的线程里被更新之后该线程可以得知该更新
        MyConf conf = new MyConf("confMsg");
        watchCallBack.setConf(conf);
        //节点存在:取出节点内的数据并更新
        //节点不存在:卡在countDownLatch,直到节点创建
        watchCallBack.aWait();
        while (true){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(conf.getConf());
        }
    }
}
