package pers.website.web.service.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import pers.website.common.pojo.dto.FeignDataDTO;
import pers.website.common.pojo.po.Book;
import pers.website.web.feign.ServerFeignClient;
import pers.website.web.service.BookService;

import java.util.List;

/**
 * 书籍服务接口实现类
 * 
 * @author ChenetChen
 * @since 2023/5/9 11:07
 */
@Service
public class BookServiceImpl implements BookService {
    @Resource
    private ServerFeignClient feignClient;

    /**
     * 通过id获取书籍
     * @param bookId 书籍id
     * @return 书籍
     */
    @Override
    public FeignDataDTO<Book> getBookById(int bookId) {
        return feignClient.getBookById(bookId);
    }

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
    @Override
    public FeignDataDTO<Integer> addBook(String bookName, String bookAuthor, String authorCountry,
                                         String pressName, String pressDate, String bookDescription,
                                         int typeId, int userId, int fileId) {
        return feignClient.addBook(bookName, bookAuthor, authorCountry,
                pressName, pressDate, bookDescription,
                typeId, userId, fileId);
    }

    /**
     * 获取热门书籍
     * @param size 列表大小
     * @return 书籍
     */
    @Override
    public FeignDataDTO<List<Book>> getHotBooks(int size) {
        return feignClient.getHotBooks(size);
    }
}
