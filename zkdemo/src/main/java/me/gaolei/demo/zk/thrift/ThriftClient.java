package me.gaolei.demo.zk.thrift;


import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

/**
 * Created by lei on 3/2/14.
 */
public class ThriftClient {

    public static void ping(String hostPort) {

        TSocket tSocket = new TSocket("localhost", 9090);
        try {
            tSocket.open();
        } catch (TTransportException e) {
            e.printStackTrace();
        }

        TProtocol protocol = new TBinaryProtocol(tSocket);
        FeatureThriftService.Client client = new FeatureThriftService.Client(protocol);


        try {
            client.ping();
        } catch (TException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        ping("");
    }
}
