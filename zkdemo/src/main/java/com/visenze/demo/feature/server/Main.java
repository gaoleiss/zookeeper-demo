package com.visenze.demo.feature.server;

/**
 * Created by lei on 2/16/14.
 */
public class Main {
    public static void main(String[] args) {
        String zkConnectionStr = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
//        FeatureService client = new FeatureService(true, new LocalInstanceContext(9090), zkConnectionStr);
        FeatureService client = new FeatureService(false, new LocalInstanceContext(9091), zkConnectionStr);
    }
}
