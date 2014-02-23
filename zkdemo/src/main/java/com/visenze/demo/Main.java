package com.visenze.demo;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.Random;

/**
 * Created by lei on 14-2-24.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        ZooKeeper zk = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183", 3000, null);
        for (int i = 0; i < 100; i++) {
            String random = String.valueOf(new Random().nextInt(10000));
            zk.create("/assignment/" + random, random.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("add assignment: " + random);
        }
    }
}
