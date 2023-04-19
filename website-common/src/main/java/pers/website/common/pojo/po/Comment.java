package pers.website.common.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论实体类
 *
 * @author ChenetChen
 * @since 2023/3/7 17:20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_notice")
public class Comment {
    /**
     * 评论id，唯一，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer commentId;
    /**
     * 发布评论用户ID
     */
    private Integer userId;
    /**
     * 评论所属博客ID
     */
    private Integer blogId;
    /**
     * 评论内容
     */
    private String commentContent;
    /**
     * 评论发布时间，yyyy-MM-dd HH:mm:ss
     */
    private String commentCreateTime;
    /**
     * 回复评论
     */
    private List<Comment> replayComments;
    /**
     * 父评论
     */
    private Comment parentComment;
    /**
     * 评论是否属于博主
     */
    private Boolean isAuthorComment;
}
