package pers.website.blog.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import pers.website.common.pojo.po.User;

/**
 * 用户服务接口
 * 
 * @author ChenetChen
 * @since 2023/3/11 12:20
 */
public interface UserService extends UserDetailsService {
    /**
     * 获取注册验证码
     * @param email 邮箱
     * @return 验证码
     */
    String registeredVerCode(String email);

    /**
     * 用户注册
     * @param userName 用户名
     * @param email 邮箱
     * @param password 密码
     * @param verCode 验证码
     * @return 注册状态
     */
    String registered(String userName, String email, String password, String verCode);

    /**
     * 获取用户注销验证码
     * @param email 邮箱
     * @return 验证码
     */
    String deleteUserVerCode(String email);

    /**
     * 用户注销
     * @param email 邮箱
     * @param password 密码
     * @param verCode 验证码
     * @return 注销结果
     */
    String deleteUser(String email, String password, String verCode);

    /**
     * 用户修改信息
     * @param userName 用户名
     * @param passwordOld 旧密码
     * @param passwordNew 新密码
     * @param phone 手机号
     * @param email 邮箱
     * @param description 简介
     * @return 结果集
     */
    String updateUser(String userName, String passwordOld, String passwordNew, String phone, String email, String description);

    /**
     * 用户登录
     * @param email 邮箱
     * @param password 密码
     * @return 结果集
     */
    User login(String email, String password);
}
