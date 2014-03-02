package me.gaolei.demo.zk.server;

import me.gaolei.demo.zk.utils.NetWorkUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by lei on 3/2/14.
 */
public class ServerTest {
    String hostPost;
    String localIp;

    @Before
    public void setUp() throws Exception {
        hostPost = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
        localIp = NetWorkUtils.getLocalIp();
//        ZooKeeper zk = zk = new ZooKeeper(hostPost, 35000, null);
    }

    @Test
    public void test() throws Exception {

        final FeatureServer server1 = new FeatureServer(hostPost, 9090);
        FeatureServer server2 = new FeatureServer(hostPost, 9091);
        FeatureServer server3 = new FeatureServer(hostPost, 9092);
//
//        final CountDownLatch latch = new CountDownLatch(1);
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    server1.start();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//
//
//        Thread.sleep(10000);
//        assertEquals(localIp + ":9090", server1.getMaster());
//        server1.close();
    }
}
