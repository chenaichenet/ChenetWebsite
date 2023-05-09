package pers.website.web.service;

import pers.website.common.pojo.dto.FeignDataDTO;
import pers.website.common.pojo.po.Book;

import java.util.List;

/**
 * 书籍服务接口
 *
 * @author ChenetChen
 * @since 2023/5/9 11:06
 */
public interface BookService {
    /**
     * 通过id获取书籍
     * @param bookId 书籍id
     * @return 书籍
     */
    FeignDataDTO<Book> getBookById(int bookId);
    
    /**
     * 添加书籍
     * @param bookName 书籍名称
     * @param bookAuthor 书籍作者
     * @param authorCountry 作者国籍
     * @param pressName 出版社
     * @param pressDate 出版日期
     * @param bookDescription 书籍简介
     * @param typeId 类型id
     * @param userId 上传用户id
     * @param fileId 文件id
     * @return 上传结果
     */
    FeignDataDTO<Integer> addBook(String bookName, String bookAuthor, String authorCountry,
                                  String pressName, String pressDate, String bookDescription,
                                  int typeId, int userId, int fileId);
    
    /**
     * 获取热门书籍
     * @param size 列表大小
     * @return 书籍
     */
    FeignDataDTO<List<Book>> getHotBooks(int size);
}
