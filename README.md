# ZookeeperApplication
Distributed locking and configuration center implemented using ZooKeeper

分布式锁:


    使用了ZooKeeper论文中锁的实现思路,创建临时序列节点,每个节点监听前一个节点的删除事件,如果
    自己前面没有节点,则获得锁,否则阻塞等待.删除锁时只需删掉持有锁的线程创建的临时序列节点即可.
    使用临时节点的原因是一旦该线程挂掉,不会一直持有锁不释放,该节点会自动销毁(比起redis的优势)



配置中心:



    多个线程监听同一ZooKeeper节点的数据更改操作,一旦数据发生变更,则更改本线程持有的配置
    