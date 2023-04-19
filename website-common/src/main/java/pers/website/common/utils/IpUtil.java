package pers.website.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pers.website.common.constants.Constants;

import java.net.InetAddress;

/**
 * IP工具类
 *
 * @author ChenetChen
 * @since 2023/3/7 12:28
 */
@Slf4j
@Component
public class IpUtil {
    /**
     * 获取IP地址
     * @param request 请求
     * @return IP地址
     */
    public String getIpAddress(HttpServletRequest request) {
        String ipAddress;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || Constants.IpConf.UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || Constants.IpConf.UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || Constants.IpConf.UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if ("127.0.0.1".equals(ipAddress)) {
                    ipAddress = InetAddress.getLocalHost().getHostAddress();
                }
            }
            //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > Constants.IpConf.IP_LENGTH && ipAddress.contains(",")) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        } catch (Exception e) {
            log.error("获取IP地址异常", e);
            ipAddress = "";
        }
        return ipAddress;
    }
}
