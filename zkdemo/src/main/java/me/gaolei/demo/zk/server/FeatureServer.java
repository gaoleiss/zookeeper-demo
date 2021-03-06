package me.gaolei.demo.zk.server;

import me.gaolei.demo.zk.Variable;
import me.gaolei.demo.zk.thrift.ThriftServer;
import me.gaolei.demo.zk.utils.LoadBalance;
import me.gaolei.demo.zk.utils.NetWorkUtils;
import org.apache.zookeeper.*;

import java.util.List;
import java.util.Vector;

/**
 * Created by lei on 3/1/14.
 */
public class FeatureServer implements Watcher {


    private ZooKeeper zk;
    private String serverId;
    private MasterSelector masterSelector;
    private int thriftPort;
    private ThriftServer thriftServer = new ThriftServer();


    public FeatureServer(String zkHostPort, int thriftPort) {
        this.thriftPort = thriftPort;
        try {
            // connect to zookeeper
            zk = new ZooKeeper(zkHostPort, 35000, this);

            // create work node and master node in zookeeper
            if (zk.exists(Variable.WORK_PATH, null) == null) {
                zk.create(Variable.WORK_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            if (zk.exists(Variable.MASTER_PATH, null) == null) {
                zk.create(Variable.MASTER_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            // watch all children  of work node
            zk.getChildren(Variable.WORK_PATH, true);

            masterSelector = new MasterSelector(zk);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws Exception {
        serverId = NetWorkUtils.getLocalIp() + ":" + thriftPort;
        //register to zookeeper
        register();

        // start thrift server
        thriftServer.startServer(thriftPort);


    }

    public void close() throws InterruptedException {
        zk.close();
        thriftServer.stop();
    }

    public String getMaster() throws KeeperException, InterruptedException {
        return masterSelector.getMaster();
    }

    private void register() {
        String path = Variable.WORK_PATH + "/" + serverId;

        try {
            zk.create(path, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, createWorkerCallback, null);
            masterSelector.update(serverId);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * the action if the children of work node changed
     *
     * @param event
     */
    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == Event.EventType.NodeChildrenChanged
                && (Variable.WORK_PATH).equals(event.getPath())) {
            try {
                updateServerList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * update load balance and master node if the children of work node changed
     *
     * @throws Exception
     */
    private void updateServerList() throws Exception {
        Vector<String> serverIdList = new Vector<String>();
        List<String> subList = zk.getChildren(Variable.WORK_PATH, true);
        for (String subNode : subList) {
            serverIdList.add(subNode);
        }

        LoadBalance.update(serverIdList);
        masterSelector.update(serverIdList);
    }

    AsyncCallback.StringCallback createWorkerCallback = new AsyncCallback.StringCallback() {
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    register();
                    break;
                case OK:
                    System.out.println("Registered successfully in " + name);
                    break;
                case NODEEXISTS:
                    System.out.println("Already registered in " + name);
                    break;
                default:
                    System.out.println("Something went wrong: " + KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };


}
