package me.gaolei.demo.zk.utils;

import java.util.Vector;

/**
 * Created by lei on 3/2/14.
 */
public class LoadBalance {
    static Vector<String> thriftList = new Vector<String>();

    public void add(String thriftHostPort) {
        thriftList.add(thriftHostPort);
    }

    public void remove(String thriftHostPort) {
        thriftList.remove(thriftHostPort);
    }

    public void update(Vector<String> thriftList) {
        LoadBalance.thriftList = thriftList;
    }

    public String get() {
        return thriftList.get(0);
    }
}
