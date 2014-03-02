package me.gaolei.demo.zk.thrift;

import org.apache.thrift.TException;

/**
 * Created by lei on 3/2/14.
 */
public class FeatureThriftServiceHandler implements FeatureThriftService.Iface {
    @Override
    public void extractFeature() throws TException {

    }

    @Override
    public String getHostPort() throws TException {
        return null;
    }

    @Override
    public void ping() throws TException {
        System.out.println("ping");
    }
}
