package com.visenze.demo.feature.server;


public class ServiceInstance {


    private String host;

    private int port = 0;


    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public ServiceInstance(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public ServiceInstance(String hostAdress) {
        int lastIndexOf = hostAdress.lastIndexOf("/");
        if (lastIndexOf >= 0) {
            hostAdress = hostAdress.substring(lastIndexOf + 1);
        }
        String[] splits = hostAdress.split(":");
        if (splits != null && splits.length == 2) {
            this.host = splits[0];
            this.port = Integer.valueOf(splits[1]);
        }
    }


    @Override
    public String toString() {
        return (this.host + ":" + this.port).toString().trim();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return toString().equals(obj.toString());
    }

}
