package pers.website.web.service;

import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import pers.website.common.pojo.dto.FeignDataDTO;
import pers.website.common.pojo.po.User;

/**
 * 用户服务接口
 *
 * @author ChenetChen
 * @since 2023/3/7 15:37
 */
public interface UserService extends UserDetailsService, UserDetailsPasswordService {
    /**
     * 获取验证码
     * @param email 邮箱
     * @return 验证码
     */
    FeignDataDTO<String> registeredVerCode(String email);

    /**
     * 用户注册
     * @param userName 用户名
     * @param email 邮箱
     * @param password 密码
     * @param passwordOk 确认密码
     * @param verCode 验证码
     * @return 注册状态
     */
    FeignDataDTO<String> registered(String userName, String email, String password, String passwordOk, String verCode);

    /**
     * 用户注销验证码
     * @param email 邮箱
     * @return 验证码
     */
    FeignDataDTO<String> deleteUserVerCode(String email);

    /**
     * 用户注销
     * @param email 邮箱
     * @param password 密码
     * @param verCode 验证码
     * @return 执行结果
     */
    FeignDataDTO<String> deleteUser(String email, String password, String verCode);

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
    FeignDataDTO<String> updateUser(String userName, String passwordOld, String password,
                                    String passwordOk, String phone, String email, String description);

    /**
     * 用户登录
     * @param email 邮箱
     * @param password 密码
     * @return 结果集
     */
    FeignDataDTO<User> login(String email, String password);
}
