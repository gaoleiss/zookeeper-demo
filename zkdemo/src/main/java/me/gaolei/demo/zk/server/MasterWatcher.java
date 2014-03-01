package me.gaolei.demo.zk.server;

import me.gaolei.demo.zk.Variable;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

/**
 * Created by lei on 3/1/14.
 */
public class MasterWatcher implements Watcher {
    private ZooKeeper zk;

    public MasterWatcher(ZooKeeper zk) {
        this.zk = zk;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("watchedEvent type: "+watchedEvent.getType());
        if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
            System.out.println(watchedEvent.getPath());
            System.out.println("feature job deleted!");
            if (watchedEvent.getPath().endsWith(getMasterNode())) {
                System.out.println("master feature job is deleted!");
                selectMaster();
            }
        }
    }

    String getMasterNode() {
        try {
            return new String(zk.getData(Variable.MASTER_PATH, false, null));
        } catch (Exception e) {
            return "";
        }
    }

    void selectMaster() {
        try {
            for (String w : zk.getChildren("/workers", false)) {
                if (isMasterExists()) {
                    zk.setData("/master", w.getBytes(), -1);
                } else {
                    zk.create("/master", w.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                }
                System.out.println("The master is :" + w);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isMasterExists() {
        boolean b = false;
        try {
            Stat stat = zk.exists("/master", false);
            if (stat != null) {
                b = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
}
