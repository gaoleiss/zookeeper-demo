package example; /**
 * Created by lei on 2/15/14.
 */

import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.*;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.ZooDefs.Ids;

import java.io.IOException;

public class Master implements Watcher {
    ZooKeeper zk;
    String hostPort;


    Master(String hostPort) {
        this.hostPort = hostPort;
    }

    void startZK() throws IOException {
        zk = new ZooKeeper(hostPort, 1000, this);
    }

    public void process(WatchedEvent e) {
        System.out.println(e.toString() + ", " + hostPort);
    }

    void register() {
        zk.create("/workers",
                "Idle".getBytes(),
                Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL,
                createWorkerCallback, null);
    }

    StringCallback createWorkerCallback = new StringCallback() {
        public void processResult(int rc, String path, Object ctx,
                                  String name) {
            switch (Code.get(rc)) {
                case CONNECTIONLOSS:
                    register();
                    break;
                case OK:
                    System.out.println("Registered successfully: ");
                    break;
                case NODEEXISTS:
                    System.out.println("Already registered: ");
                    break;
                default:
                    System.out.println("Something went wrong: "
                            + KeeperException.create(Code.get(rc), path));
            }
        }
    };

    public static void main(String[] args)
            throws Exception {
//        String hostPort = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
        String hostPort = "172.31.50.62:2888,172.31.50.62:2182,172.31.50.62:2183";
        Master m = new Master(hostPort);
        m.startZK();
        m.register();
        Thread.sleep(60000);
    }
}


