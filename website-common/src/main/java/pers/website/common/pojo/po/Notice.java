package pers.website.common.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公告实体类
 *
 * @author ChenetChen
 * @since 2023/3/7 12:20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_notice")
public class Notice {
    /**
     * 公告id，唯一，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer noticeId;
    /**
     * 发布公告用户ID
     */
    private Integer userId;
    /**
     * 公告标题
     */
    private String noticeTitle;
    /**
     * 公告内容
     */
    private String noticeContent;
    /**
     * 公告发布时间，yyyy-MM-dd HH:mm:ss
     */
    private String noticeCreateTime;
    /**
     * 公告状态，0：正常；-1：失效
     */
    private Integer noticeState;
}
