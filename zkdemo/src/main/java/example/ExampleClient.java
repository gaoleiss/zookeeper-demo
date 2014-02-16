package example; /**
 * Created by lei on 2/16/14.
 */

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.*;
import org.apache.zookeeper.CreateMode;

/**
 * An example leader selector client. Note that {@link LeaderSelectorListenerAdapter} which
 * has the recommended handling for connection state issues
 */
public class ExampleClient {

    public static void main(String[] args) throws Exception {
        String hostPort = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
        CuratorFramework zkClient = CuratorFrameworkFactory.builder().connectString(hostPort).retryPolicy(new
                RetryOneTime(Integer.MAX_VALUE)).connectionTimeoutMs(5000).build();
        zkClient.start();
        zkClient.create().withMode(CreateMode.EPHEMERAL).forPath("/head", "hello world".getBytes());
        zkClient.close();
    }

}