package me.gaolei.demo.zk.server;

import me.gaolei.demo.zk.Variable;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.util.Vector;

/**
 * Created by lei on 3/1/14.
 */
public class MasterSelector {
    private ZooKeeper zk;

    public MasterSelector(ZooKeeper zk) {

        this.zk = zk;
    }

    public String getMaster() throws KeeperException, InterruptedException {

        return new String(zk.getData(Variable.MASTER_PATH, false, null));

    }

    void setMasterServerId(String serverId) throws KeeperException, InterruptedException {
        zk.setData(Variable.MASTER_PATH, serverId.getBytes(), -1);
        System.out.println("master update:\t" + serverId);
    }

    boolean isMasterExists() throws KeeperException, InterruptedException {
        boolean ret = false;
        String masterServerId = getMaster();

        for (String child : zk.getChildren(Variable.WORK_PATH, false)) {
            if (child.equals(masterServerId)) {
                return true;
            }
        }

        return ret;
    }

    public void update(String serverId) throws KeeperException, InterruptedException {
        if (!isMasterExists()) {
            setMasterServerId(serverId);
        }

    }


    public void update(Vector<String> serverList) {

        for (String serverId : serverList) {
            try {
                update(serverId);
                break;
            } catch (Exception e) {
                continue;
            }

        }
    }
}
