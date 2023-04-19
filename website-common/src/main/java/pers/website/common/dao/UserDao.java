package pers.website.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pers.website.common.pojo.po.User;

import java.util.List;

/**
 * 用户表
 * 
 * @author ChenetChen
 * @since 2023/3/7 14:49
 */
@Mapper
public interface UserDao extends BaseMapper<User> {
    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<User> findAll();

    /**
     * 查询用户
     * @param user 用户
     * @return 用户列表
     */
    List<User> findByFilter(User user);

    /**
     * 新增用户
     * @param user 用户
     * @return 新增结果
     */
    int insertUser(User user);

    /**
     * 更新用户
     * @param user 用户
     * @return 更新结果
     */
    int updateUser(User user);

    /**
     * 删除用户
     * @param user 用户
     * @return 删除结果
     */
    int deleteUser(User user);
}
