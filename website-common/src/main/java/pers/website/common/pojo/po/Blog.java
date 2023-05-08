package pers.website.common.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 博客实体类
 *
 * @author ChenetChen
 * @since 2023/5/8 10:36
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_blog")
public class Blog {
    /**
     * 博客id，唯一，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer blogId;
    /**
     * 博客标题
     */
    private String blogTitle;
    /**
     * 发布时间：yyyy-MM-dd HH:mm:ss
     */
    private String blogCreateTime;
    /**
     * 博客浏览量
     */
    private Integer blogViews;
    /**
     * 博客简介
     */
    private String blogDescription;
    /**
     * 博客首页图
     */
    private String blogPicture;
    /**
     * 博客内容
     */
    private String blogContent;
    /**
     * 博客作者
     */
    private Integer userId;
    /**
     * 博客类型
     */
    private Integer typeId;
}
