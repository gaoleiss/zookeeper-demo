package me.gaolei.demo.zk.server;

import me.gaolei.demo.zk.utils.ServiceNetWorkUtils;
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
        localIp = ServiceNetWorkUtils.getLocalIp();
    }

    @Test
    public void test() throws Exception {

        new Server(hostPost, 9090).start();
    }
}
