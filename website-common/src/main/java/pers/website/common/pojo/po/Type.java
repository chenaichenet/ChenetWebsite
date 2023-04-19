package pers.website.common.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类型实体类
 *
 * @author ChenetChen
 * @since 2023/3/7 11:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_type")
public class Type {
    /**
     * 类型id，唯一，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer typeId;
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 类型描述
     */
    private String typeDescription;
    /**
     * 类型创建时间，yyyy-MM-dd HH:mm:ss
     */
    private String typeCreateTime;
    /**
     * 类型状态，0：有效；-1：无效
     */
    private Integer typeState;
}
