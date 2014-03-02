package me.gaolei.demo.zk.client;

import me.gaolei.demo.zk.server.MasterSelector;
import me.gaolei.demo.zk.thrift.ThriftClient;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * Created by lei on 3/2/14.
 */
public class FeatureClient {
    private ZooKeeper zk;
    private MasterSelector masterSelector;
    private ThriftClient thriftClient = new ThriftClient();

    public FeatureClient(String zkHostPort) throws IOException {
        zk = new ZooKeeper(zkHostPort, 35000, null);
        masterSelector = new MasterSelector(zk);

    }

    public void process() throws Exception {
        // get master in zookeeper
        String master = masterSelector.getMaster();
        // get the job thrift hostport from master thrift server
        String thriftHostPort = thriftClient.getHostPort(master);
        // extract feature
        thriftClient.extractFeature(thriftHostPort);
        System.out.println("extract feature from " + thriftHostPort);
    }
}
