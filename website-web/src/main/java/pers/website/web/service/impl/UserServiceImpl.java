package pers.website.web.service.impl;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pers.website.common.constants.Constants;
import pers.website.common.pojo.dto.FeignDataDTO;
import pers.website.common.pojo.po.User;
import pers.website.common.utils.MailUtils;
import pers.website.common.utils.ParamUtils;
import pers.website.web.feign.ServerFeignClient;
import pers.website.web.service.UserService;


/**
 * 用户相关服务实现类
 *
 * @author ChenetChen
 * @since 2023/3/7 15:40
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private ServerFeignClient feignClient;

    /**
     * 获取验证码
     * @param email 邮箱
     * @return 验证码
     */
    @Override
    public FeignDataDTO<String> registeredVerCode(String email) {
        if (Boolean.TRUE.equals(MailUtils.checkMail(email))) {
            return feignClient.registeredVerCode(email);
        } else {
            return FeignDataDTO.<String>builder()
                    .state(Constants.FEIGN_STATE_INTERCEPT)
                    .message("邮箱格式错误").build();
        }
    }

    /**
     * 用户注册
     * @param userName 用户名
     * @param password 密码
     * @param passwordOk 确认密码
     * @param verCode 验证码
     * @return 注册状态
     */
    @Override
    public FeignDataDTO<String> registered(String userName, String email, String password, String passwordOk, String verCode) {
        if (password.equals(passwordOk)) {
            if (Boolean.TRUE.equals(MailUtils.checkMail(email))) {
                return feignClient.registered(userName, email, password, verCode);
            } else {
                return FeignDataDTO.<String>builder()
                        .state(Constants.FEIGN_STATE_INTERCEPT)
                        .message("邮箱格式错误").build();
            }
        } else {
            return FeignDataDTO.<String>builder()
                    .state(Constants.FEIGN_STATE_INTERCEPT)
                    .message("两次输入密码不一致").build();
        }
    }

    /**
     * 用户注销验证码
     * @param email 邮箱
     * @return 验证码
     */
    @Override
    public FeignDataDTO<String> deleteUserVerCode(String email) {
        if (Boolean.TRUE.equals(MailUtils.checkMail(email))) {
            return feignClient.deleteUserVerCode(email);
        } else {
            return FeignDataDTO.<String>builder()
                    .state(Constants.FEIGN_STATE_INTERCEPT)
                    .message("邮箱格式错误").build();
        }
    }

    /**
     * 用户注销
     * @param email 邮箱
     * @param password 密码
     * @param verCode 验证码
     * @return 执行结果
     */
    @Override
    public FeignDataDTO<String> deleteUser(String email, String password, String verCode) {
        if (StringUtils.isEmpty(verCode)) {
            return FeignDataDTO.<String>builder()
                    .state(Constants.FEIGN_STATE_INTERCEPT)
                    .message("邮箱格式错误").build();
        } else {
            return feignClient.deleteUser(email, password, verCode);
        }
    }

    /**
     * 用户信息修改
     * @param userName 用户名
     * @param passwordOld 用户旧密码
     * @param password 用户密码
     * @param passwordOk 用户密码
     * @param phone 用户手机
     * @param email 用户邮箱
     * @param description 用户简介
     * @return 修改结果集
     */
    @Override
    public FeignDataDTO<String> updateUser(String userName, String passwordOld, String password,
                                           String passwordOk, String phone, String email, String description) {
        if (ParamUtils.isNotEmpty(true, passwordOld, password, passwordOk) && !password.equals(passwordOk)) {
            return FeignDataDTO.<String>builder()
                    .state(Constants.FEIGN_STATE_INTERCEPT)
                    .message("两次输入新密码不一致").build();
        }
        return feignClient.updateUser(userName, passwordOld, password, passwordOk, phone, email, description);
    }

    /**
     * 用户登录
     * @param email 邮箱
     * @param password 密码
     * @return 结果集
     */
    @Override
    public FeignDataDTO<User> login(String email, String password) {
        if (Boolean.TRUE.equals(MailUtils.checkMail(email))){
            return feignClient.login(email, password);
        } else {
            return FeignDataDTO.<User>builder()
                    .state(Constants.FEIGN_STATE_INTERCEPT)
                    .message("邮箱格式错误").build();
        }
    }
    
    /**
     * 重写Security中用户认证方法
     * @param username 用户名
     * @return 用户对象
     * @throws UsernameNotFoundException 用户名异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("用户名为空");
        }
        FeignDataDTO<? super User> userFeignDataDTO = feignClient.loadUserByUsername(username);
        if (userFeignDataDTO != null && Constants.FEIGN_STATE_SUCCESS.equals(userFeignDataDTO.getState())) {
            return (UserDetails) userFeignDataDTO.getResult();
        }
        return null;
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }
}
