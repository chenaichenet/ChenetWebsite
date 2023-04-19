package pers.website.blog.consult;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pers.website.blog.service.UserService;
import pers.website.common.constants.Constants;
import pers.website.common.pojo.dto.FeignDataDTO;
import pers.website.common.pojo.po.User;


/**
 * 博客用户相关请求
 * 
 * @author ChenetChen
 * @since 2023/3/11 12:18
 */
@RestController
@RequestMapping("/user")
public class UserConsult {
    @Resource
    private UserService userService;

    /**
     * 获取注册验证码
     * @param email 邮箱
     * @return 验证码
     */
    @RequestMapping(value = "/registeredVerCode", method = RequestMethod.POST)
    public FeignDataDTO<Object> registeredVerCode(String email){
        String verCode = userService.registeredVerCode(email);
        if (ObjectUtils.isNotEmpty(verCode)) {
            switch (verCode) {
                case "0-2":
                    return FeignDataDTO.builder().state(Constants.FEIGN_STATE_ERROR).message("邮箱已经注册").build();
                case "0-3":
                    return FeignDataDTO.builder().state(Constants.FEIGN_STATE_ERROR).message("缓存验证码失败").build();
                case "-1":
                    return FeignDataDTO.builder().state(Constants.FEIGN_STATE_ERROR).message("发送邮件失败").build();
                default:
                    return FeignDataDTO.builder().state(Constants.FEIGN_STATE_SUCCESS).message("获取验证码成功").result(verCode).build();
            }
        } else {
            return FeignDataDTO.builder().state(Constants.FEIGN_STATE_ERROR).message("获取验证码失败").build();
        }
    }

    /**
     * 用户注册
     *
     * @param userName 用户名
     * @param password 密码
     * @param verCode 验证码
     * @return 远程调用结果集
     */
    @RequestMapping("/registered")
    public FeignDataDTO<String> registered(String userName, String email, String password, String verCode) {
        String registered = userService.registered(userName, email, password, verCode);
        if (ObjectUtils.isNotEmpty(registered)) {
            switch (registered) {
                case "1":
                    return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_SUCCESS).message("用户注册成功").build();
                case "0-3":
                    return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_ERROR).message("邮箱已经注册").build();
                case "0-4":
                    return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_ERROR).message("验证码错误").build();
                default:
                    return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_ERROR).message("用户注册异常").build();
            }
        } else {
            return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_ERROR).message("用户注册异常").build();
        }
    }

    /**
     * 获取用户注销验证码
     * @param email 邮箱
     * @return 验证码
     */
    @RequestMapping("/deleteUserVerCode")
    public FeignDataDTO<String> deleteUserVerCode(String email) {
        String verCode = userService.deleteUserVerCode(email);
        if (ObjectUtils.isNotEmpty(verCode)) {
            switch (verCode) {
                case "0-2":
                    return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_ERROR).message("缓存验证码失败").build();
                case "-1":
                    return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_ERROR).message("发送邮件失败").build();
                default:
                    return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_SUCCESS).message("获取验证码成功").result(verCode).build();
            }
        } else {
            return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_ERROR).message("获取验证码失败").build();
        }
    }

    /**
     * 用户注销
     * @param email 邮箱
     * @param password 密码
     * @param verCode 验证码
     * @return 注销结果
     */
    @RequestMapping("/deleteUser")
    public FeignDataDTO<String> deleteUser(String email, String password, String verCode) {
        String deleteUser = userService.deleteUser(email, password, verCode);
        if (ObjectUtils.isNotEmpty(deleteUser)) {
            switch (deleteUser) {
                case "0-2":
                    return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_ERROR).message("获取缓存验证码失败").build();
                case "0-3":
                    return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_ERROR).message("没有该用户").build();
                case "0-4":
                    return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_ERROR).message("用户密码错误").build();
                case "1":
                    return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_SUCCESS).message("用户注销成功").build();
                case "-1":
                default:
                    return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_ERROR).message("用户注销失败").build();
            }
        } else {
            return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_ERROR).message("用户注销失败").build();
        }
    }

    /**
     * 用户修改信息
     * @param userName 用户名
     * @param passwordOld 旧密码
     * @param passwordNew 新密码
     * @param phone 手机号
     * @param email 邮箱
     * @param description 简介
     * @return 修改结果
     */
    @RequestMapping("/updateUser")
    public FeignDataDTO<String> updateUser(String userName, String passwordOld, String passwordNew,
                                           String phone, String email, String description) {
        String updateUser = userService.updateUser(userName, passwordOld, passwordNew, phone, email, description);
        if (ObjectUtils.isNotEmpty(updateUser)) {
            switch (updateUser) {
                case "0-2":
                    return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_ERROR).message("用户查询异常").build();
                case "0-3":
                    return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_ERROR).message("用户密码错误，修改密码失败").build();
                case "1":
                    return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_SUCCESS).message("用户修改信息成功").build();
                default:
                    return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_ERROR).message("用户修改信息异常").build();
            }
        } else {
            return FeignDataDTO.<String>builder().state(Constants.FEIGN_STATE_ERROR).message("用户修改信息失败").build();
        }
    }

    /**
     * 用户登录
     * @param email 邮箱
     * @param password 密码
     * @return 结果集
     */
    @RequestMapping("/login")
    public FeignDataDTO<User> login(String email, String password) {
        User user = userService.login(email, password);
        if (user != null) {
            return FeignDataDTO.<User>builder().state(Constants.FEIGN_STATE_SUCCESS).message("登录成功").result(user).build();
        } else {
            return FeignDataDTO.<User>builder().state(Constants.FEIGN_STATE_ERROR).message("登录失败").result(null).build();
        }
    }

    /**
     * 重写Security认证方法
     * @param userName 用户名
     * @return 远程调用结果
     */
    @RequestMapping("/loadUserByUsername")
    public FeignDataDTO<? super User> loadUserByUsername(String userName) {
        UserDetails userDetails = userService.loadUserByUsername(userName);
        if (userDetails != null) {
            return FeignDataDTO.builder().state(Constants.FEIGN_STATE_ERROR).message("加载用户失败").result(userDetails).build();
        } else {
            return FeignDataDTO.builder().state(Constants.FEIGN_STATE_ERROR).message("加载用户失败").result(null).build();
        }
    }
}
