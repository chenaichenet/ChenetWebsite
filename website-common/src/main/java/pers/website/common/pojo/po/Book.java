package pers.website.common.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 书籍实体类
 *
 * @author ChenetChen
 * @since 2023/5/8 10:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_book")
public class Book {
    /**
     * 书籍id，唯一，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer bookId;
    /**
     * 书籍名
     */
    private String bookName;
    /**
     * 书籍作者
     */
    private String bookAuthor;
    /**
     * 作者国籍
     */
    private String authorCountry;
    /**
     * 出版社
     */
    private String pressName;
    /**
     * 出版时间：yyyy-MM-dd
     */
    private String pressDate;
    /**
     * 书籍简介
     */
    private String bookDescription;
    /**
     * 书籍类型
     */
    private Integer typeId;
    /**
     * 书籍上传用户
     */
    private Integer userId;
    /**
     * 书籍文件
     */
    private Integer fileId;
}
