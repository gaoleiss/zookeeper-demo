package me.gaolei.demo.zk.thrift;

import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * Created by lei on 3/2/14.
 */
public class ThriftServer {

    TServer server;

    public void start(int port) throws TTransportException {

        TServerTransport serverTransport = new TServerSocket(port);
        TProcessor processor = new FeatureThriftService.Processor<FeatureThriftServiceHandler>(new FeatureThriftServiceHandler());

        server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

        System.out.println("start thrift server");
        server.serve();

    }

    public void stop() {
        if (server != null) {
            server.stop();
        }
    }

    public static void main(String[] args) throws TTransportException {
        new ThriftServer().start(9090);
    }
}
