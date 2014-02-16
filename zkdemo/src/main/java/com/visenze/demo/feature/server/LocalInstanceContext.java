package com.visenze.demo.feature.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalInstanceContext implements InstanceContext {

	private int port = -1;
	
	public LocalInstanceContext(int port) {
		this.port = port;
	}

	public String getHostName(){
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return "";
		}
	}
	
	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}
}
