package me.gaolei.demo.zk.utils;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Vector;

/**
 * Created by lei on 3/2/14.
 */
public class LoadBalanceTest {
    @Test
    public void test() throws Exception {
        Vector<String> thriftList = new Vector<>();
        thriftList.add("localhost:9091");
        thriftList.add("localhost:9092");
        thriftList.add("localhost:9093");
        thriftList.add("localhost:9094");
        thriftList.add("localhost:9095");
        LoadBalance.update(thriftList);

        for (int i = 0; i < 100; i++) {
            TestCase.assertTrue(thriftList.contains(LoadBalance.get()));
        }
    }
}
