package pers.website.common.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * 权限实体类
 *
 * @author ChenetChen
 * @since 2023/3/7 11:06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_roles")
public class Roles implements GrantedAuthority {
    /**
     * 角色ID，唯一，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer roleId;
    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;
    /**
     * 角色创建时间，yyyy-MM-dd HH:mm:ss
     */
    private String roleCreateTime;
    /**
     * 角色状态，0：正常；-1：禁用
     */
    private Integer roleState;
    
    @Override
    public String getAuthority() {
        return this.roleName;
    }
}
