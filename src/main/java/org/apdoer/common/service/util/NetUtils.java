package org.apdoer.common.service.util;


import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import net.sf.json.JSONObject;

public class NetUtils {
//    /**
//     * 获取登录用户IP地址
//     *
//     * @param request
//     * @return
//     */
//    public static String getIpAddr(HttpServletRequest request) {
//        String ip = request.getHeader("x-forwarded-for");
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//        }
//        if (ip.equals("0:0:0:0:0:0:0:1")) {
//            ip = "127.0.0.1";
//        }
//        return ip;
//    }
//
//    /**
//     * 获取登录用户IP地址
//     *
//     * @param request
//     * @return
//     */
//    public static String getIp(HttpServletRequest request) {
//        String ip = request.getHeader("X-Forwarded-For");
//        if (("".equals(ip) || ip == null) && !"unKnown".equalsIgnoreCase(ip)) {
//            //多次反向代理后会有多个ip值，第一个ip才是真实ip
//            int index = ip.indexOf(",");
//            if (index != -1) {
//                return ip.substring(0, index);
//            } else {
//                return ip;
//            }
//        }
//        ip = request.getHeader("X-Real-IP");
//        if (("".equals(ip) || ip == null) && !"unKnown".equalsIgnoreCase(ip)) {
//            return ip;
//        }
//        return request.getRemoteAddr();
//    }
//
//
//    /**
//     * 获取登录用户IP地址
//     *
//     * @param request
//     * @return
//     */
//    public static String getIp2(HttpServletRequest request) {
//        // 获取ip地址
//        String ip = request.getHeader("x-forwarded-for");
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("HTTP_CLIENT_IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//        }
//        System.out.println(ip);
//        // 因为有些有些登录是通过代理，所以取第一个（第一个为真是ip）
//        int index = ip.indexOf(',');
//        if (index != -1) {
//            ip = ip.substring(0, index);
//        }
//        return ip;
//    }


    /**
     * 获取Ip地址
     *
     * @param request
     * @return
     */
    public static String getIpAdrress(HttpServletRequest request) {
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = XFor.indexOf(",");
            if (index != -1) {
                XFor = XFor.substring(0, index);
            }
            if ("0:0:0:0:0:0:0:1".equals(XFor)) {
                XFor = "127.0.0.1";
            }
            return XFor;
        }
        XFor = Xip;
        if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            return XFor;
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getRemoteAddr();
        }
        if ("0:0:0:0:0:0:0:1".equals(XFor)) {
            XFor = "127.0.0.1";
        }
        return XFor;
    }


    /**
     * 获取登录用户地址
     *
     * @param
     * @return
     */
    public static String getAddByIp(String ip) {
        //淘宝IP地址库：http://ip.taobao.com/instructions.php
        String add = null;
        if ("127.0.0.1".equals(ip)) {
            return "本机";
        }
        try {
            //URL U = new URL("http://ip.taobao.com/service/getIpInfo.php?ip=114.111.166.72");
            URL U = new URL("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);
            URLConnection connection = U.openConnection();
            connection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            in.close();

            JSONObject jsonObject = JSONObject.fromObject(result);
            Map<String, Object> map = (Map) jsonObject;
            String code = String.valueOf(map.get("org/apdoer/common/service/code"));
            if ("1".equals(code)) {//失败
                String data = String.valueOf(map.get("data"));
            } else if ("0".equals(code)) {//成功
                Map<String, Object> data = (Map<String, Object>) map.get("data");

                String country = String.valueOf(data.get("country"));//国家
                String area = String.valueOf(data.get("area"));//
                String city = String.valueOf(data.get("city")); //市（县）
                String region = String.valueOf(data.get("region"));//省（自治区或直辖市）
                add = getLocation(country, region, city);

            }
            return add;
        } catch (MalformedURLException e1) {
            add = "未知";

        } catch (IOException e) {
            add = "未知";

        }
        return add;
    }

    private static String getLocation(String country, String region, String city) {
        StringBuffer location = new StringBuffer("");
        if (!"XX".equals(location)) {
            location.append(country);
        }
        if (!"XX".equals(region)) {
            location.append(region);
        }
        if (!"XX".equals(city)) {
            location.append(city);
        }
        return location.toString();
    }

}