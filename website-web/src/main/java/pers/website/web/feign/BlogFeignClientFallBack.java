package pers.website.web.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pers.website.common.constants.Constants;
import pers.website.common.pojo.dto.FeignDataDTO;
import pers.website.common.pojo.po.User;

import java.util.List;

/**
 * 博客相关请求熔断处理类
 * 
 * @author ChenetChen
 * @since 2023/3/7 15:50
 */
@Slf4j
@Component
public class BlogFeignClientFallBack implements BlogFeignClient{
    @Override
    public FeignDataDTO<String> registeredVerCode(String email) {
        log.error("获取注册验证码出现熔断");
        return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_ERROR).message("获取注册验证码出现熔断").build();
    }

    @Override
    public FeignDataDTO<String> registered(String userName, String email, String password, String verCode) {
        log.error("用户注册出现熔断");
        return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_ERROR).message("用户注册出现熔断").build();
    }

    @Override
    public FeignDataDTO<String> deleteUserVerCode(String email) {
        log.error("获取注销验证码获取出现熔断");
        return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_ERROR).message("获取注销验证码获取出现熔断").build();
    }

    @Override
    public FeignDataDTO<String> deleteUser(String email, String password, String verCode) {
        log.error("用户注销出现熔断");
        return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_ERROR).message("用户注销出现熔断").build();
    }

    @Override
    public FeignDataDTO<String> updateUser(String userName, String passwordOld, String password, String passwordOk, String phone, String email, String description) {
        log.error("用户修改信息出现熔断");
        return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_ERROR).message("用户修改信息出现熔断").build();
    }

    @Override
    public FeignDataDTO<User> login(String email, String password) {
        log.error("用户登录出现熔断");
        return FeignDataDTO.<User>builder().state(Constants.FEIGN_STATE_ERROR).message("用户登录出现熔断").build();
    }

    @Override
    public FeignDataDTO<UserDetails> loadUserByUsername(String username) {
        return FeignDataDTO.<UserDetails>builder().state(Constants.FEIGN_STATE_ERROR).message("用户登录出现熔断").build();
    }
}
