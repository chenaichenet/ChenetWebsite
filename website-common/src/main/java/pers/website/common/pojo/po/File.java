package pers.website.common.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件实体类
 *
 * @author ChenetChen
 * @since 2023/5/8 11:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_file")
public class File {
    /**
     * 文件id，唯一，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer fileId;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件类型
     */
    private Integer typeId;
    /**
     * 文件地址（相对路径或链接）
     */
    private String fileAddress;
    /**
     * 文件上传时间：yyyy-MM-dd HH:mm:ss
     */
    private String fileCreateTime;
    /**
     * 文件状态，0：有效；-1：无效
     */
    private Integer fileState;
}
