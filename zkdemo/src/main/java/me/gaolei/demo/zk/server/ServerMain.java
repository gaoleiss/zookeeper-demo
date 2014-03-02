package me.gaolei.demo.zk.server;

/**
 * Created by lei on 3/2/14.
 */
public class ServerMain {
    public static void main(String[] args) throws Exception {
        String hostPost = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
//        if (args.length < 1) {
//            System.out.println("Usage: thrift_port");
//        }
//        FeatureServer server1 = new FeatureServer(hostPost, Integer.valueOf(args[0]));
        FeatureServer server1 = new FeatureServer(hostPost, 9092);
        server1.start();
    }
}
