package pers.website.common.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 用户实体类
 *
 * @author ChenetChen
 * @since 2023/3/7 10:49
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class User implements UserDetails {
    /**
     * 用户ID，唯一，采用自增
     */
    @TableId(type = IdType.AUTO)
    private Integer userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户头像，固定头像素材列表，采用id对应
     */
    private Integer userAvatar;
    /**
     * 用户等级
     */
    private Integer userLevel;
    /**
     * 用户密码
     */
    private String userPassword;
    /**
     * 用户邮箱，唯一
     */
    private String userEmail;
    /**
     * 用户权限，对应权限表
     */
    private List<Roles> userRolesList;
    /**
     * 用户创建时间：yyyy-MM-dd HH:mm:ss
     */
    private String userCreateTime;
    /**
     * 用户简介
     */
    private String userDescription;
    /**
     * 用户状态，0：可用；-1：禁用
     */
    private Integer userState;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRolesList;
    }

    @Override
    public String getPassword() {
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
