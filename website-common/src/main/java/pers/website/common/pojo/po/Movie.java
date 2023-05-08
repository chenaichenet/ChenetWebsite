package pers.website.common.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 电影实体类
 * 
 * @author ChenetChen
 * @since 2023/5/8 10:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_movie")
public class Movie {
    /**
     * 电影id，唯一，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer movieId;
    /**
     * 电影名
     */
    private String movieName;
    /**
     * 导演名
     */
    private String directorName;
    /**
     * 导演国家
     */
    private String directorCountry;
    /**
     * 上映时间：yyyy-MM-dd
     */
    private String pressDate;
    /**
     * 电影简介
     */
    private String movieDescription;
    /**
     * 电影类型
     */
    private Integer typeId;
    /**
     * 电影上传用户
     */
    private Integer userId;
}
