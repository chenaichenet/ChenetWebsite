package pers.website.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.website.blog.service.UserService;
import pers.website.common.constants.Constants;
import pers.website.common.dao.RoleDao;
import pers.website.common.dao.UserDao;
import pers.website.common.dao.UserRoleDao;
import pers.website.common.enums.ExceptionEnum;
import pers.website.common.exceptions.CustomException;
import pers.website.common.pojo.po.Roles;
import pers.website.common.pojo.po.User;
import pers.website.common.pojo.po.UserRoles;
import pers.website.common.utils.MailUtil;
import pers.website.common.utils.ParamUtil;
import pers.website.common.utils.RedisUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 * 
 * @author ChenetChen
 * @since 2023/3/11 12:21
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;
    
    @Resource
    private UserRoleDao userRoleDao;
    
    @Resource
    private RoleDao roleDao;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 获取注册验证码
     * @param email 邮箱
     * @return 验证码
     */
    @Override
    public String registeredVerCode(String email) {
        Long count = userDao.selectCount(new QueryWrapper<User>().eq("user_email", email));
        if (count == 0) {
            String verCode = ParamUtil.newVerCode(6);
            if (Boolean.TRUE.equals(redisUtil.hashPut(Constants.RedisKey.REDIS_REGISTER_VER_CODE, email, verCode, 120))) {
                MailUtil.sendMail(email, "注册", verCode);
                return verCode;
            } else {
                log.error("缓存验证码失败");
                return "0-3";
            }
        } else {
            log.info("邮箱已经注册");
            return "0-2";
        }
    }

    /**
     * 用户注册
     * @param userName 用户名
     * @param password 密码
     * @param verCode 验证码
     * @return 注册状态
     */
    @Override
    @Transactional(rollbackFor = CustomException.class)
    public String registered(String userName, String email, String password, String verCode) {
        Long count = userDao.selectCount(new QueryWrapper<User>().lambda().eq(User::getUserEmail, email));
        if (count == 0) {
            Object redisVerCode = redisUtil.hashGet("registeredVerCode", email);
            if (ObjectUtils.isNotEmpty(verCode) && verCode.equals(redisVerCode)) {
                ArrayList<Roles> rolesArrayList = new ArrayList<>();
                rolesArrayList.add(Roles.builder().roleId(1).build());
                String dateFormat = new SimpleDateFormat("yyyyMMdd").format(new Date());
                User user = User.builder()
                        .userName(userName)
                        .userEmail(email)
                        .userPassword(new BCryptPasswordEncoder().encode(password))
                        .userRolesList(rolesArrayList)
                        .userCreateTime(dateFormat)
                        .userState(1).build();
                int insert = userDao.insert(user);
                if (insert < 0) {
                    throw new CustomException(ExceptionEnum.TRANSACTIONAL_ROLLBACK);
                } else {
                    return "1";
                }
            } else {
                log.info("验证码错误");
                return "0-4";
            }
        } else {
            log.info("邮箱已经注册");
            return "0-3";
        }
    }

    /**
     * 获取用户注销验证码
     * @param email 邮箱
     * @return 验证码
     */
    @Override
    public String deleteUserVerCode(String email) {
        String verCode = ParamUtil.newVerCode(6);
        if (Boolean.TRUE.equals(redisUtil.hashPut(Constants.RedisKey.REDIS_DELETE_VER_CODE, email, verCode, 120))) {
            MailUtil.sendMail(email, "注销", verCode);
            return verCode;
        } else {
            log.error("缓存验证码失败");
            return "0-2";
        }
    }

    /**
     * 用户注销
     * @param email 邮箱
     * @param password 密码
     * @param verCode 验证码
     * @return 注销状态
     */
    @Override
    public String deleteUser(String email, String password, String verCode) {
        Object redisVerCode = redisUtil.hashGet("deleteUserVerCode", email);
        if (ObjectUtils.isNotEmpty(redisVerCode) && redisVerCode.equals(verCode)) {
            List<User> userList = userDao.findByFilter(User.builder().userEmail(email).build());
            if (userList.size() == 1) {
                User user = userList.get(0);
                if (user.getPassword().equals(new BCryptPasswordEncoder().encode(password))) {
                    int delete = userDao.delete(new QueryWrapper<User>().lambda().eq(User::getUserEmail, email));
                    if (delete == 1) {
                        log.info("用户注销成功");
                        return "1";
                    } else {
                        log.error("用户注销失败");
                        return "-1";
                    }
                } else {
                    log.error("用户密码错误");
                    return "0-4";
                }
            } else {
                log.error("没有该用户");
                return "0-3";
            }
        } else {
            log.error("获取缓存验证码失败");
            return "0-2";
        }
    }

    /**
     * 用户修改信息
     * @param userName 用户名
     * @param passwordNew 密码
     * @param phone 手机号
     * @param email 邮箱
     * @param description 简介
     * @return 结果集
     */
    @Override
    public String updateUser(String userName,String passwordOld, String passwordNew, String phone, String email, String description) {
        List<User> userList = userDao.findByFilter(User.builder().userEmail(email).build());
        if (!userList.isEmpty() && userList.size() != 1) {
            User oldUser = userList.get(0);
            if (ObjectUtils.isEmpty(passwordOld) || !oldUser.getPassword().equals(passwordOld)) {
                log.error("密码错误，更新密码失败");
                return "0-3";
            }
            User newUser = User.builder().userName(userName).userPassword(passwordNew).userEmail(email).userDescription(description).build();
            int update = userDao.update(newUser, new QueryWrapper<User>().lambda().eq(User::getUserId, oldUser.getUserId()));
            if (update == 1) {
                log.info("用户修改信息成功");
                return "1";
            } else {
                log.error("用户修改信息失败");
                return "-1";
            }
        } else {
            log.error("用户查询出现异常：没有该用户或存在多个用户");
            return "0-2";
        }
    }

    /**
     * 用户登录
     * @param email 邮箱
     * @param password 密码
     * @return 结果集
     */
    @Override
    public User login(String email, String password) {
        List<User> userList = userDao.findByFilter(User.builder().userEmail(email).userPassword(password).build());
        if (!userList.isEmpty() && userList.size() != 1) {
            return userList.get(0);
        } else {
            log.error("用户查询出现异常：没有该用户或存在多个用户");
            return null;
        }
    }

    /**
     * 重写Security中用户认证方法
     * @param username 用户名
     * @return 用户对象
     * @throws UsernameNotFoundException 用户名异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            log.error("用户名为空");
        }
        User userByUsername = userDao.selectOne(new QueryWrapper<User>().lambda().eq(User::getUsername, username));
        User userByEmail = userDao.selectOne(new QueryWrapper<User>().lambda().eq(User::getUserEmail, username));
        if (userByUsername == null && userByEmail == null) {
            log.error("用户不存在");
        } else if (userByUsername != null && userByEmail == null) {
            getUserRoleList(userByUsername);
            return userByUsername;
        } else if (userByUsername == null) {
            getUserRoleList(userByEmail);
            return userByEmail;
        }
        return null;
    }

    /**
     * 获取用户权限列表
     * @param user 用户
     */
    private void getUserRoleList(User user) {
        List<UserRoles> userRoles = userRoleDao.selectList(new QueryWrapper<UserRoles>().lambda().eq(UserRoles::getUserId, user.getUserId()));
        if (ObjectUtils.isNotEmpty(userRoles)) {
            List<Integer> roleIdList = userRoles.stream().map(UserRoles::getRoleId).collect(Collectors.toList());
            List<Roles> roles = roleDao.selectList(new QueryWrapper<Roles>().lambda().in(Roles::getRoleId, roleIdList));
            user.setUserRolesList(roles);
        }
    }
}
