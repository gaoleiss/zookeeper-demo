package me.gaolei.demo.zk.client;

/**
 * Created by lei on 3/2/14.
 */
public class ClientMain {
    public static void main(String[] args) throws Exception {
        String hostPost = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
        new FeatureClient(hostPost).process();
    }
}
