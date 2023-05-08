package pers.website.common.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 音乐实体类
 *
 * @author ChenetChen
 * @since 2023/5/8 11:09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_music")
public class Music {
    /**
     * 音乐id，唯一，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer musicId;
    /**
     * 音乐名
     */
    private String musicName;
    /**
     * 歌手名
     */
    private String singerName;
    /**
     * 歌手国籍
     */
    private String singerCountry;
    /**
     * 发行时间：yyyy-MM-dd
     */
    private String pressDate;
    /**
     * 音乐简介
     */
    private String musicDescription;
    /**
     * 音乐类型
     */
    private Integer typeId;
    /**
     * 音乐上传用户
     */
    private Integer userId;
    /**
     * 音乐文件
     */
    private Integer fileId;
}
