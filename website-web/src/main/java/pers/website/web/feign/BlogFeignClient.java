package pers.website.web.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pers.website.common.pojo.dto.FeignDataDTO;
import pers.website.common.pojo.po.User;

/**
 * 博客相关请求远程调用接口
 * 
 * @author ChenetChen
 * @since 2023/3/7 15:49
 */
@FeignClient(name = "Website-Blog", fallback = BlogFeignClientFallBack.class)
public interface BlogFeignClient {

    /**
     * 注册验证码
     * @param email 邮箱
     * @return 验证码
     */
    @RequestMapping(value = "/user/registeredVerCode", method = RequestMethod.POST)
    FeignDataDTO<String> registeredVerCode(@RequestParam("email") String email);

    /**
     * 用户注册
     * @param userName 用户名
     * @param email 邮箱
     * @param password 密码
     * @param verCode 验证码
     * @return 注册结果
     */
    @RequestMapping(value = "/user/registered", method = RequestMethod.POST)
    FeignDataDTO<String> registered(@RequestParam("userName") String userName,
                                    @RequestParam("email") String email,
                                    @RequestParam("password") String password,
                                    @RequestParam("verCode") String verCode);

    /**
     * 用户注销验证码
     * @param email 邮箱
     * @return 验证码
     */
    @RequestMapping(value = "/user/deleteUserVerCode", method = RequestMethod.POST)
    FeignDataDTO<String> deleteUserVerCode(@RequestParam("email") String email);

    /**
     * 用户注销
     * @param email 邮箱
     * @param password 密码
     * @param verCode 验证码
     * @return 注销结果
     */
    @RequestMapping(value = "/user/deleteUser", method = RequestMethod.POST)
    FeignDataDTO<String> deleteUser(@RequestParam("email") String email,
                                    @RequestParam("password") String password,
                                    @RequestParam("verCode") String verCode);

    /**
     * 用户修改信息
     * @param userName 用户名
     * @param passwordOld 旧密码
     * @param password 密码
     * @param passwordOk 确认密码
     * @param phone 手机号
     * @param email 邮箱
     * @param description 简介
     * @return 修改结果
     */
    @RequestMapping(value = "/user/updateUser")
    FeignDataDTO<String> updateUser(@RequestParam("userName") String userName,
                                    @RequestParam("passwordOld") String passwordOld,
                                    @RequestParam("password") String password,
                                    @RequestParam("passwordOk") String passwordOk,
                                    @RequestParam("phone") String phone,
                                    @RequestParam("email") String email,
                                    @RequestParam("description") String description);

    /**
     * 用户登录
     * @param email 邮箱
     * @param password 密码
     * @return 结果集
     */
    @RequestMapping(value = "/user/login")
    FeignDataDTO<User> login(@RequestParam("email") String email,
                             @RequestParam("password") String password);

    /**
     * SpringSecurity校验
     * @param userName 用户名
     * @return 用户
     */
    @RequestMapping(value = "/user/loadUserByUsername")
    FeignDataDTO<? super User> loadUserByUsername(@RequestParam("userName") String userName);
}
