package me.gaolei.demo.zk.thrift;

import me.gaolei.demo.zk.utils.LoadBalance;
import org.apache.thrift.TException;

/**
 * Created by lei on 3/2/14.
 */
public class FeatureThriftServiceHandler implements FeatureThriftService.Iface {
    @Override
    public void extractFeature() throws TException {
        System.out.println("extract feature...");
    }

    @Override
    public String getHostPort() throws TException {
        return LoadBalance.get();
    }

    @Override
    public void ping() throws TException {
        System.out.println("ping");
    }
}
