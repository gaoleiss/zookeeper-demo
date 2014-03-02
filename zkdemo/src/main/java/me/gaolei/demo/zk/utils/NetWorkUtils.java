package me.gaolei.demo.zk.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by lei on 3/2/14.
 */
public class NetWorkUtils {
    public static String getLocalIp() {
        String ret = null;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface current = interfaces.nextElement();
                if (!current.isUp() || current.isLoopback() || current.isVirtual())
                    continue;
                Enumeration<InetAddress> addresses = current.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress current_addr = addresses.nextElement();
                    if (current_addr instanceof Inet4Address) {
                        ret = current_addr.getHostAddress();
                    }
                }
                break;
            }
        } catch (Exception e) {
            ret = null;
        }

        return ret;
    }
}
