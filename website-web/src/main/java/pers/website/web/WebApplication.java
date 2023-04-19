package pers.website.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * website-web模块启动类
 *
 * @author ChenetChen
 * @since 2023/03/07 11:23
 */
@SpringBootApplication(scanBasePackages = {"pers.website.web","pers.website.common"})
@EnableFeignClients(basePackages = "pers.website.web.feign")
@EnableDiscoveryClient
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class);
    }
}