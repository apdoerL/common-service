package org.apdoer.common.service.util;


import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressUtils {
    private static String locaHostName;
    private static String localHostIp;

    private InetAddressUtils() {
    }

    /**
     * 获取本机地址信息
     *
     * @return InetAddress
     */
    public static InetAddress getInetAddress() throws UnknownHostException {

        return InetAddress.getLocalHost();

    }

    /**
     * 获取本机IP
     *
     * @return String
     */
    public static String getHostIp() throws UnknownHostException {
        if (null == localHostIp) {
            InetAddress inetAddress = getInetAddress();
            if (null != inetAddress) {
                localHostIp = inetAddress.getHostAddress();
            }
        }
        return localHostIp;
    }

    /**
     * 获取本机HOSTNAME
     *
     * @return String
     */
    public static String getHostName() throws UnknownHostException {
        if (null == locaHostName) {
            InetAddress inetAddress = getInetAddress();
            if (null != inetAddress) {
                locaHostName = inetAddress.getHostName();
                if (null == locaHostName) {
                    locaHostName = getHostIp();
                }
            }
        }
        return locaHostName;
    }
}
