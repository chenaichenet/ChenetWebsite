package pers.website.common.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户权限实体类
 *
 * @author ChenetChen
 * @since 2023/3/7 11:09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user_role")
public class UserRoles {
    /**
     * ID，唯一，采用自增
     */
    @TableId(type = IdType.AUTO)
    private Integer userRoleId;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 角色ID
     */
    private Integer roleId;
    /**
     * 是否永久，true：永久；false：临时
     */
    private Boolean isPermanent;
    /**
     * 生效时间：yyyy-MM-dd HH:mm:ss
     */
    private String effectiveTime;
    /**
     * 失效时间：yyyy-MM-dd HH:mm:ss
     */
    private String ineffectiveTime;
    /**
     * 是否有效，0：有效；-1：无效
     */
    private Integer userRoleState;
}
