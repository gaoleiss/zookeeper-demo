package me.gaolei.demo.zk.server;

/**
 * Created by lei on 3/1/14.
 */
public class Server {
    public static void main(String[] args) throws Exception {
        String hostPost = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
        new Worker(hostPost).start();
    }
}
