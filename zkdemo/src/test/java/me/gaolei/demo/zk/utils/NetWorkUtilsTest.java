package me.gaolei.demo.zk.utils;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by lei on 3/2/14.
 */
public class NetWorkUtilsTest {
    @Test
    public void testGetLocalIp() throws Exception {

        TestCase.assertNotNull(NetWorkUtils.getLocalIp());
    }
}
