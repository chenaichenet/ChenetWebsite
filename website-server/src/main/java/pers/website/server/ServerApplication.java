package pers.website.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * website-blog模块启动类
 *
 * @author ChenetChen
 * @since 2023/03/07 11:23
 */
@SpringBootApplication(scanBasePackages = "pers.website.*")
@EnableDiscoveryClient
@MapperScan(basePackages = {"pers.website.common.dao"})
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class);
    }
}