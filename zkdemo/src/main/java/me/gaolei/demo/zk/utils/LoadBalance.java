package me.gaolei.demo.zk.utils;

import java.util.Random;
import java.util.Vector;

/**
 * Created by lei on 3/2/14.
 */
public class LoadBalance {
    static Vector<String> thriftList = new Vector<String>();


    public static void update(Vector<String> thriftList) {
        System.out.println("lb update:\t" + thriftList);
        LoadBalance.thriftList = thriftList;
    }

    public static String get() {

        int index = new Random().nextInt(thriftList.size());
        return thriftList.get(index);
    }
}
