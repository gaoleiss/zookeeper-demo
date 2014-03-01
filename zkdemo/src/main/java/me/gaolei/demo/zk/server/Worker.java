package me.gaolei.demo.zk.server;

import me.gaolei.demo.zk.Variable;
import org.apache.zookeeper.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by lei on 3/1/14.
 */
public class Worker implements Watcher {


    private ZooKeeper zk;
    private String serverId;

    public Worker(String hostPort) {

        try {
            serverId = Integer.toString(new Random().nextInt());
            zk = new ZooKeeper(hostPort, 35000, this);
            zk.getChildren(Variable.WORK_PATH, true);

            if (zk.exists(Variable.WORK_PATH, null) == null) {
                zk.create(Variable.WORK_PATH, "thrift_port:thrift_host".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            if (zk.exists(Variable.MASTER_PATH, null) == null) {
                zk.create(Variable.MASTER_PATH, "thrift_port:thrift_host".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }


        } catch (KeeperException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws Exception {

        //register to zookeeper
        register();

        // start thrift server
        while (true) {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void register() {
        zk.create(Variable.WORK_PATH + "/" + serverId, "idle".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, createWorkerCallback, null);

    }


    AsyncCallback.StringCallback createWorkerCallback = new AsyncCallback.StringCallback() {
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    register();
                    break;
                case OK:
                    System.out.println("Registered successfully in " + path);
                    break;
                case NODEEXISTS:
                    System.out.println("Already registered in " + path);
                    break;
                default:
                    System.out.println("Something went wrong: " + KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };


    @Override
    public void process(WatchedEvent event) {
        System.out.println("event:"+event.getType());
        if (event.getType() == Event.EventType.NodeChildrenChanged
                && (Variable.WORK_PATH).equals(event.getPath())) {
            try {
                updateServerList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void updateServerList() throws Exception {
        List<String> newServerList = new ArrayList<String>();
        List<String> subList = zk.getChildren(Variable.WORK_PATH, true);
        for (String subNode : subList) {
            newServerList.add(subNode);
        }

        System.out.println("server list updated: " + newServerList);
    }
}
